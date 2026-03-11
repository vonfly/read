# 模板：Navigation / Hilt Module / Network / DataStore

## Navigation 类型安全路由（Navigation ≥ 2.8）

```kotlin
// navigation/AppRoutes.kt
@Serializable object BookListRoute
@Serializable data class ReaderRoute(val bookId: String)
@Serializable data class SettingsRoute(val from: String = "")

// navigation/AppNavHost.kt
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = BookListRoute) {
        composable<BookListRoute> {
            BookListScreen(
                onBookClick = { id -> navController.navigate(ReaderRoute(id)) },
                onSettingsClick = { navController.navigate(SettingsRoute()) }
            )
        }
        composable<ReaderRoute> {
            ReaderScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable<SettingsRoute> {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
```

## Hilt Module 模板

```kotlin
// di/DatabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration()  // 开发期；生产环境改为 addMigrations()
            .build()

    @Provides
    fun provideXxxDao(db: AppDatabase): XxxDao = db.xxxDao()
}

// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindXxxRepository(impl: XxxRepositoryImpl): XxxRepository
}
```

## NetworkModule 模板

```kotlin
// di/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideOkHttpClient(
        tokenProvider: TokenProvider  // 接口注入，避免静态持有 token
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = tokenProvider.getToken()  // 每次请求时动态获取，支持 token 刷新
                val request = chain.request().newBuilder()
                    .apply { if (token != null) addHeader("Authorization", "Bearer $token") }
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true   // 后端新增字段不会导致解析崩溃
        coerceInputValues = true   // null 字段使用默认值
    }

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)  // TODO: 在 build.gradle.kts 的 buildConfigField 中配置实际地址
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides @Singleton
    fun provideBookApiService(retrofit: Retrofit): BookApiService =
        retrofit.create(BookApiService::class.java)
}

// TokenProvider 接口（在 di/ 中用 @Binds 绑定实现）
interface TokenProvider {
    fun getToken(): String?
}
```

⚠️ 禁止在 Interceptor 里直接引用静态对象的 token，token 更新后旧请求会携带过期凭证。

## DataStoreModule 模板

项目中有多个 DataStore 实例时，必须用 `@Qualifier` 区分，禁止直接注入裸 `DataStore<Preferences>`
（否则 Hilt 无法区分两个同类型实例，编译报歧义错误）。

```kotlin
// di/qualifier/DataStoreQualifiers.kt
// 每个 DataStore 实例对应一个 Qualifier 注解，语义清晰、编译期安全
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppDataStore      // 用户通用偏好（主题、语言等）

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReaderDataStore   // 阅读器专属设置（字体、行距、背景色）

// di/DataStoreModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // 用户通用偏好 → 文件名 "app_settings"
    @Provides @Singleton @AppDataStore
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("app_settings") }
        )

    // 阅读器设置 → 文件名 "reader_settings"（独立文件，便于单独清除或迁移）
    @Provides @Singleton @ReaderDataStore
    fun provideReaderDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("reader_settings") }
        )
}
// ⚠️ 同一进程内每个文件名只能有一个 DataStore 实例，@Singleton 保证这一点
// ⚠️ 禁止新增第三个实例，新的偏好必须复用以上两个（在 CLAUDE.md 中有约定）
// 若需要结构化数据（非简单键值），改用 Proto DataStore，单独创建 ProtoDataStoreModule
```
