# 模板：数据层（Repository / Room / DataStore）

## Repository 模板

```kotlin
// domain/repository/XxxRepository.kt（接口）
interface XxxRepository {
    fun observeAll(): Flow<List<Xxx>>
    suspend fun fetchRemote(id: String): Result<Xxx>
}

// data/repository/XxxRepositoryImpl.kt（实现）
class XxxRepositoryImpl @Inject constructor(
    private val dao: XxxDao,
    private val api: XxxApiService
) : XxxRepository {

    override fun observeAll(): Flow<List<Xxx>> =
        dao.getAll().map { it.map(XxxEntity::toDomain) }

    override suspend fun fetchRemote(id: String): Result<Xxx> = try {
        Result.success(api.getXxx(id).toDomain())
    } catch (e: Exception) {
        if (e is CancellationException) throw e  // 必须重新抛出
        Result.failure(e)
    }
}
```

## Room Entity + DAO 模板

```kotlin
@Entity(tableName = "xxx")
data class XxxEntity(
    @PrimaryKey val id: String,
    val title: String,
    val updatedAt: Long = System.currentTimeMillis()
)

@Dao
interface XxxDao {
    @Query("SELECT * FROM xxx ORDER BY updatedAt DESC")
    fun getAll(): Flow<List<XxxEntity>>   // 必须是 Flow，禁止返回 List

    @Upsert  suspend fun upsert(entity: XxxEntity)
    @Delete  suspend fun delete(entity: XxxEntity)

    @Query("SELECT * FROM xxx WHERE id = :id")
    suspend fun getById(id: String): XxxEntity?
}
```

## DataStore 模板

```kotlin
// 通用偏好 Repository —— 注入 @AppDataStore 实例（文件名 "app_settings"）
// 存放与阅读器无关的用户偏好：界面语言、通知开关等
class AppPreferencesRepository @Inject constructor(
    @AppDataStore private val dataStore: DataStore<Preferences>
) {
    val fontSize: Flow<Float> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[FONT_SIZE] ?: 16f }

    suspend fun setFontSize(size: Float) {
        dataStore.edit { it[FONT_SIZE] = size }
    }

    companion object {
        val FONT_SIZE = floatPreferencesKey("font_size")
    }
}

// 阅读器偏好 Repository —— 注入 @ReaderDataStore 实例（文件名 "reader_settings"）
// 存放阅读器专属设置：字体大小、行距、背景色等（详见 docs/templates/reader.md）
class ReaderPreferencesRepository @Inject constructor(
    @ReaderDataStore private val dataStore: DataStore<Preferences>
) {
    // 具体实现见 docs/templates/reader.md
}

// ⚠️ 两个 Repository 必须使用对应的 @Qualifier 注入，禁止注入裸 DataStore<Preferences>
// ⚠️ @AppDataStore 和 @ReaderDataStore 定义在 di/qualifier/DataStoreQualifiers.kt
// 若需要多字段原子读写（如同时存字体+主题+行距），改用 Proto DataStore
```
