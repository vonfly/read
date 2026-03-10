# 模板：阅读器专属

## 自定义阅读主题（CompositionLocal）

阅读 App 的字体大小、行距、背景色独立于系统 Material Theme，用 `CompositionLocal` 管理：

```kotlin
// ui/theme/ReaderTheme.kt
data class ReaderSettings(
    val fontSize: TextUnit = 16.sp,
    val lineHeight: TextUnit = 24.sp,
    val letterSpacing: TextUnit = 0.sp,
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default
)

enum class ReaderColorScheme(val background: Color, val text: Color) {
    Default(Color(0xFFFAF8F5), Color(0xFF1A1A1A)),   // 米白
    Night  (Color(0xFF1C1C1E), Color(0xFFE5E5EA)),    // 夜间
    Sepia  (Color(0xFFF4ECD8), Color(0xFF5B4636)),    // 护眼暖色
    Green  (Color(0xFFCCE8CC), Color(0xFF1A3A1A)),    // 绿色护眼
}

val LocalReaderSettings = compositionLocalOf { ReaderSettings() }

@Composable
fun ReaderTheme(
    settings: ReaderSettings,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalReaderSettings provides settings) {
        content()
    }
}

@Composable
fun ReaderTextBlock(text: String) {
    val settings = LocalReaderSettings.current
    Text(
        text = text,
        fontSize = settings.fontSize,
        lineHeight = settings.lineHeight,
        color = settings.colorScheme.text
    )
}
```

## 阅读设置持久化（DataStore）

```kotlin
// data/local/preferences/ReaderPreferencesRepository.kt
class ReaderPreferencesRepository @Inject constructor(
    @ReaderDataStore private val dataStore: DataStore<Preferences>  // 独立文件 "reader_settings"
) {
    val readerSettings: Flow<ReaderSettings> = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { prefs ->
            ReaderSettings(
                fontSize = prefs[FONT_SIZE]?.sp ?: 16.sp,
                lineHeight = prefs[LINE_HEIGHT]?.sp ?: 24.sp,
                colorScheme = ReaderColorScheme.entries
                    .find { it.name == prefs[COLOR_SCHEME] } ?: ReaderColorScheme.Default
            )
        }

    suspend fun updateFontSize(size: Float) = dataStore.edit { it[FONT_SIZE] = size }
    suspend fun updateColorScheme(scheme: ReaderColorScheme) =
        dataStore.edit { it[COLOR_SCHEME] = scheme.name }

    companion object {
        val FONT_SIZE    = floatPreferencesKey("reader_font_size")
        val LINE_HEIGHT  = floatPreferencesKey("reader_line_height")
        val COLOR_SCHEME = stringPreferencesKey("reader_color_scheme")
    }
}
```

## 长文本渲染优化策略

Compose 中直接渲染完整书籍内容会导致严重性能问题，必须分段：

```kotlin
// domain/model/PageContent.kt
data class PageContent(
    val pageIndex: Int,
    val paragraphs: List<String>,
    val chapterTitle: String? = null
)

// 渲染原则：
// 1. 后台线程预处理：将原始文本按屏幕高度分页（不在主线程做字符串分割）
// 2. LazyColumn 只渲染当前页，不要把整本书的 paragraph 全塞进一个 LazyColumn
// 3. 翻页用 HorizontalPager（Compose Foundation），不用自定义手势

@Composable
fun ReaderPageContent(page: PageContent) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalReaderSettings.current.colorScheme.background)
    ) {
        page.chapterTitle?.let {
            item(key = "chapter_title") {
                Text(it, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
            }
        }
        items(
            items = page.paragraphs,
            key = { "${page.pageIndex}_$it".hashCode() }
        ) { paragraph ->
            ReaderTextBlock(paragraph)
            Spacer(Modifier.height(8.dp))
        }
    }
}
```

## 阅读进度持久化（两层策略）

> **设计原则**：WorkManager 是为"应用退出后仍需可靠执行"的工作设计的，
> 不适合前台高频写入。进度保存采用两层策略，各司其职：
>
> - **第一层（前台）**：翻页时直接用协程写入 Room，响应快、无系统调度开销
> - **第二层（后台）**：应用进入后台时，用 WorkManager 将最新进度同步到服务端，
>   保证即使进程被杀也能完成同步

### 第一层：协程直写 Room（前台翻页时）

```kotlin
// domain/repository/ReadingProgressRepository.kt（接口）
interface ReadingProgressRepository {
    suspend fun saveLocalProgress(bookId: String, page: Int, position: Float)
    suspend fun syncProgressToRemote(bookId: String): Result<Unit>
}

// ViewModel 中调用（防抖：停止翻页 500ms 后写入，避免每次翻页都触发 IO）
private var saveJob: Job? = null

fun onPageChange(page: Int) {
    _uiState.update { it.copy(currentPage = page) }
    saveJob?.cancel()
    saveJob = viewModelScope.launch {
        delay(500)  // 短防抖：仅防止每帧都写 IO，不是 30 秒长延迟
        progressRepository.saveLocalProgress(bookId, page, 0f)
    }
}
```

### 第二层：WorkManager 同步到服务端（进入后台时）

```kotlin
// data/work/SyncProgressWorker.kt
@HiltWorker
class SyncProgressWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val progressRepository: ReadingProgressRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val bookId = inputData.getString(KEY_BOOK_ID) ?: return Result.failure()
        return progressRepository.syncProgressToRemote(bookId)
            .fold(
                onSuccess = { Result.success() },
                onFailure = { e ->
                    if (e is CancellationException) throw e
                    Result.retry()  // 网络失败自动重试
                }
            )
    }

    companion object {
        const val KEY_BOOK_ID = "book_id"

        fun buildRequest(bookId: String): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<SyncProgressWorker>()
                .setInputData(workDataOf(KEY_BOOK_ID to bookId))
                .setConstraints(
                    Constraints(requiredNetworkType = NetworkType.CONNECTED)
                )
                .build()
    }
}

// ViewModel 中：监听 Lifecycle，进入后台时触发同步
// 在 ReaderScreen 中通过 LifecycleEventObserver 或 OnStop 回调触发：
fun onReaderStop(bookId: String) {
    val request = SyncProgressWorker.buildRequest(bookId)
    workManager.enqueue(request)
}
```

```kotlin
// ReaderScreen.kt 中监听生命周期
val lifecycleOwner = LocalLifecycleOwner.current
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_STOP) {
            viewModel.onReaderStop(bookId)
        }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
}
```

> 如果项目没有服务端同步需求，可以省略第二层，只保留第一层协程直写 Room 即可。

## 屏幕常亮（阅读时防锁屏）

```kotlin
@Composable
fun KeepScreenOn(enabled: Boolean = true) {
    val context = LocalContext.current
    DisposableEffect(enabled) {
        val window = (context as? Activity)?.window
        if (enabled) window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

// 在阅读器 Screen 顶层调用
@Composable
fun ReaderScreen(/* ... */) {
    KeepScreenOn(enabled = true)  // 进入阅读器自动常亮，离开自动恢复
    // ...
}
```

> `context as? Activity` 在绝大多数情况下安全（Compose 运行在 Activity 内）。
> 如果 context 不是 Activity，`?.window` 为 null，常亮设置静默失败，不会崩溃。
