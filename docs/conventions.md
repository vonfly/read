# 低频场景约定

## UseCase 规范

每个 UseCase 只暴露一个 `invoke`，确保单一职责：

```kotlin
class GetXxxUseCase @Inject constructor(private val repo: XxxRepository) {
    suspend operator fun invoke(id: String): Result<Xxx> = repo.getXxx(id)
}
// 禁止在同一 UseCase 里定义多个方法，应拆分为独立的 UseCase
```

## Hilt EntryPoint（非 Hilt 管理的组件）

适用场景：WorkManager Worker、自定义 ContentProvider、部分 Service。
优先使用 `@HiltWorker + @AssistedInject`，只有无法用 HiltWorker 时才用 EntryPoint。

```kotlin
@EntryPoint
@InstallIn(SingletonComponent::class)
interface XxxEntryPoint {
    fun xxxRepository(): XxxRepository
}

// 在 Worker 或其他非 Hilt 组件中获取依赖
val repo = EntryPointAccessors
    .fromApplication(applicationContext, XxxEntryPoint::class.java)
    .xxxRepository()
```

## 沉浸式阅读模式（隐藏系统栏）

```kotlin
// rememberWindowInsetsController 来自 androidx.activity.compose
// 需要在 libs.versions.toml 中确认 compose-activity 版本 ≥ 1.8.0
import androidx.activity.compose.rememberWindowInsetsController
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

// MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // 必须在 setContent 之前
        setContent { AppTheme { AppNavHost() } }
    }
}

// 沉浸式切换（在阅读器 Screen 中使用）
@Composable
fun ImmersiveController(isImmersive: Boolean) {
    val windowInsetsController = rememberWindowInsetsController()
    LaunchedEffect(isImmersive) {
        if (isImmersive) {
            windowInsetsController?.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            windowInsetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}
```

## WindowInsets 特殊设备适配

Android 15 起系统强制全屏，所有屏幕内容默认延伸到系统栏后面。
折叠屏、刘海屏、打孔屏等设备如果不处理 Insets，内容会被遮挡。

**通用原则：优先用 `Scaffold`，它自动处理绝大多数场景。**
只有在不用 `Scaffold` 的全屏 UI（如阅读器、相机）才需要手动处理。

```kotlin
// 场景一：阅读器全屏内容区，需要避开刘海和打孔屏
@Composable
fun ReaderFullScreenContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.displayCutout)  // 避开刘海/打孔区域
            .windowInsetsPadding(WindowInsets.safeDrawing)    // 避开系统栏
    ) {
        // 阅读内容
    }
}

// 场景二：底部工具栏（阅读设置面板），需要避开导航栏
@Composable
fun ReaderBottomBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)  // 避开底部导航栏
    ) {
        // 工具栏内容
    }
}

// 场景三：折叠屏适配（检测折叠状态）
// 需要额外引入 androidx.window:window 库，在 libs.versions.toml 中添加：
//
// [versions]
// androidx-window = "1.3.0"
//
// [libraries]
// androidx-window = { group = "androidx.window", name = "window", version.ref = "androidx-window" }
//
// 然后在 app/build.gradle.kts dependencies 中：
// implementation(libs.androidx.window)
//
// 折叠屏开发优先参考 WindowManager 官方文档
```

**不同 Insets 类型的选择：**

| 场景 | 推荐 Insets |
|------|------------|
| 普通页面（有 Scaffold） | Scaffold 的 `innerPadding` |
| 全屏页面顶部 | `WindowInsets.statusBars` |
| 全屏页面底部 | `WindowInsets.navigationBars` |
| 有刘海/打孔屏的全屏 | `WindowInsets.displayCutout` |
| 全屏安全区域（推荐默认）| `WindowInsets.safeDrawing` |

---

## AppError 错误模型（可选，按需启用）

适用场景：需要在 UI 层区分不同错误类型并给出差异化提示时（如网络断开 vs 未登录 vs 本地读取失败）。
若项目错误处理较简单，直接用 `Result<T>` 的 `Throwable.message` 即可，不必引入此模型。

```kotlin
// domain/model/AppError.kt
sealed interface AppError {
    val message: String

    // 网络错误：HTTP 状态码 + 服务端返回信息
    data class Network(
        val code: Int,
        override val message: String
    ) : AppError

    // 未登录或 token 失效，需要跳转登录页
    data object Unauthorized : AppError {
        override val message = "登录已过期，请重新登录"
    }

    // 本地存储错误（Room 读写失败、DataStore 异常等）
    data class Storage(
        override val message: String,
        val cause: Throwable? = null
    ) : AppError

    // 未知错误兜底
    data class Unknown(
        override val message: String = "未知错误，请稍后重试",
        val cause: Throwable? = null
    ) : AppError
}

// Repository 使用方式：failure 里包装 AppError 而非原始 Throwable
suspend fun fetchBook(id: String): Result<Book> = try {
    Result.success(api.getBook(id).toDomain())
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    if (e.code() == 401) Result.failure(AppError.Unauthorized)
    else Result.failure(AppError.Network(e.code(), e.message()))
} catch (e: Exception) {
    Result.failure(AppError.Unknown(cause = e))
}

// ViewModel 中区分错误类型
.onFailure { error ->
    val msg = when (error) {
        is AppError.Unauthorized -> {
            _event.send(UiEvent.NavigateTo(LoginRoute))
            return@onFailure
        }
        is AppError.Network  -> "网络错误 ${error.code}：${error.message}"
        is AppError.Storage  -> "本地数据读取失败"
        else                 -> "操作失败，请重试"
    }
    _event.send(UiEvent.ShowSnackbar(msg))
}
```

---

## Android 15 16KB 内存页适配

自 2026 年 5 月起，Google Play 要求所有应用必须支持 16KB 内存页。
**该要求主要影响含 NDK（C/C++）代码的应用。**

### 本项目的情况

本项目（阅读 App）使用纯 Kotlin/Java 技术栈：
- epublib → 纯 Java
- PdfRenderer → Android 系统 API
- TXT 解析 → 纯 Kotlin
- Room / Hilt / Retrofit 等 → 纯 Java/Kotlin

**纯 Kotlin/Java 代码本身不受影响**，但部分第三方库内部可能包含 native 代码。

### 验证第三方库是否已适配

```bash
# 方法一：检查 APK 中的 .so 文件是否支持 16KB 对齐
# 在 Android Studio 中：Build → Analyze APK → 查看 lib/ 目录下的 .so 文件
# 使用命令行工具：
objdump -p your_lib.so | grep LOAD  # 查看 PT_LOAD segment 的对齐值，应为 16384

# 方法二：在 16KB 页模拟器上运行
# AVD Manager 中创建支持 16KB 页的模拟器（API 35+，arm64_16k 系统镜像）
adb shell getconf PAGESIZE  # 输出 16384 表示模拟器使用 16KB 页
```

### 若引入了含 native 代码的第三方库

1. 升级到该库已适配 16KB 的最新版本（大多数主流库在 2025 年已完成适配）
2. 在 16KB 页模拟器上跑一遍完整功能，确认无崩溃
3. 若库未适配且无替代方案，联系库作者或提 Issue

### 若将来项目引入 NDK

- NDK 版本升级至 **r27 或更高**
- 禁止硬编码页大小：~~`val PAGE_SIZE = 4096`~~ → 动态获取 `Os.sysconf(OsConstants._SC_PAGESIZE)`
- 所有 native 库需用支持 16KB 对齐的 NDK 重新编译

---

## Compose 事件处理：避免 Snackbar 阻塞事件流

这是一个**经典错误**：在 `LaunchedEffect` 中处理事件时，`snackbarHostState.showSnackbar()` 会阻塞协程，导致后续事件无法被处理。

### ❌ 错误写法

```kotlin
LaunchedEffect(Unit) {
    viewModel.event.collect { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)  // 阻塞！
            UiEvent.NavigateBack -> onNavigateBack()  // Snackbar 显示期间无法处理
        }
    }
}
```

**问题原因：**
1. `collect` 是**顺序处理**事件的
2. `showSnackbar()` 是**挂起函数**，会阻塞等待 Snackbar 被关闭
3. 协程被阻塞后，后续事件在 Channel 中排队等待
4. 用户点击返回 → `NavigateBack` 事件发送 → 但协程还在等 Snackbar 关闭 → **没反应**

### ✅ 正确写法

```kotlin
val scope = rememberCoroutineScope()

LaunchedEffect(Unit) {
    viewModel.event.collect { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> {
                // 使用 launch 异步显示，不阻塞事件流
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
            UiEvent.NavigateBack -> onNavigateBack()  // 立即处理
            is UiEvent.NavigateToXxx -> onNavigateToXxx(...)
        }
    }
}
```

**关键点：**
- `scope.launch` 创建新协程异步显示 Snackbar
- 主事件流不被阻塞，后续事件（如导航）可以立即处理
- 适用于所有**非关键性 UI 反馈**（Snackbar、Toast 等）
- **关键性操作**（如导航）应该直接同步处理，不要用 `launch`

---

## 底部组件阴影向上投射（BottomBar / BottomSheet）

### 问题

Android Compose 的 `Modifier.shadow()` 和 `graphicsLayer { shadowElevation }` 阴影**只向下投射**。
当组件位于屏幕底部时（如 BottomBar、BottomSheet），阴影会投射到屏幕外被裁掉，视觉上看不到阴影效果。

### 尝试过但不生效的方案

| 方案 | 阴影方向 | 问题 |
|------|----------|------|
| `Modifier.shadow(elevation, shape)` | 向下 | 被屏幕底部裁掉 |
| `graphicsLayer { shadowElevation, clip = true }` | 向下 | 同上 |
| `drawBehind` + `setShadowLayer(dy = 正数)` | 向下 | 同上 |

### 解决方案

使用 `drawBehind` + `setShadowLayer(dy = 负数)` 手动画向上投射的阴影：

```kotlin
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas

Box(
    modifier = modifier
        .fillMaxWidth()
        .height(132.dp)
        .drawBehind {
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    asFrameworkPaint().apply {
                        isAntiAlias = true
                        color = android.graphics.Color.TRANSPARENT
                        setShadowLayer(
                            16f,   // blur radius
                            0f,    // dx
                            -6f,   // dy 负数 = 向上投射
                            android.graphics.Color.argb(40, 0, 0, 0)  // 阴影颜色
                        )
                    }
                }
                canvas.drawRoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    radiusX = 16.dp.toPx(),
                    radiusY = 16.dp.toPx(),
                    paint = paint
                )
            }
        }
        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        .background(Color(0xFFF9F9F9))
) {
    // 内容
}
```

### 关键点

1. `setShadowLayer` 的 `dy` 参数：**负数 = 向上投射**，正数 = 向下投射
2. 必须先 `drawBehind` 画阴影，再 `clip` + `background` 裁剪和填充背景
3. `Modifier.shadow()` 和 `graphicsLayer` 无法控制阴影方向，只向下

### 注意事项

- `setShadowLayer` 在某些低端设备或特定 Android 版本上可能表现不一致
- 如果只需要简单阴影且组件不在屏幕边缘，优先用 `Modifier.shadow()`
- 只有当组件在屏幕底部需要向上投射阴影时，才用此方案

### 相关文件

- `ui/screen/reader/components/ReaderBottomBar.kt`

---

## Compose 指针事件传播与消费机制

### 问题场景

嵌套组件都需要处理点击事件时，如何控制事件传播和消费？
典型场景：阅读器外层处理翻页/显示控制栏，内层控制栏需要阻止事件传播到外层。

### PointerEventPass 传播方向

```
Initial  → 父→子（外层先处理）
Main     → 子→父（内层先处理）← 默认
Final    → 子→父（内层先处理）
```

### 错误方案

| 尝试 | 方案 | 失败原因 |
|-----|------|---------|
| 1 | 内层用 `combinedClickable` | `onClick` 即使是空的也会消费事件，但外层用 `Initial` pass 先处理 |
| 2 | 外层用 `Initial` pass | `Initial` 是**父→子**，外层永远先处理 |
| 3 | 内层用 `clickable` | `clickable` 用 `Main` pass，但外层用 `Initial` pass 先处理 |
| 4 | 内层也用 `Initial` pass | `Initial` 是**父→子**，外层还是先处理 |

### ✅ 正确方案

**内外层都用 `Main` pass（默认），内层先收到事件并 `consume()`：**

```kotlin
// 外层：使用 Main pass（默认），requireUnconsumed=true 只接收未消费事件
Box(
    modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown()  // 默认 requireUnconsumed=true
                val up = waitForUpOrCancellation()
                if (up != null) {
                    // 处理翻页/显示控制栏
                }
            }
        }
) {
    // 内层控制栏：使用 Main pass（子→父），先于外层处理
    AnimatedVisibility(visible = isControlsVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()  // 先收到 down 事件
                        down.consume()               // 消费 down
                        val up = waitForUpOrCancellation()
                        up?.consume()                // 消费 up
                        // 事件已消费，外层的 awaitFirstDown(requireUnconsumed=true) 不会收到
                    }
                }
        ) {
            // 控制栏内容...
        }
    }
}
```

### 事件传播流程图

```
用户点击控制栏区域
    │
    ▼ (Main pass: 子→父)
┌─────────────────────────────────┐
│ 内层 Box 先收到                  │
│   awaitFirstDown() → consume()  │
│   waitForUpOrCancellation()     │
│   up.consume()                  │
└─────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────┐
│ 外层 Box 后收到                  │
│   awaitFirstDown()              │
│   → 事件已被消费，不触发         │
└─────────────────────────────────┘
```

### 关键 API

```kotlin
// 接收 down 事件
awaitFirstDown(requireUnconsumed: Boolean = true)  // 默认只接收未消费事件
awaitFirstDown(pass: PointerEventPass = PointerEventPass.Main)  // 指定 pass

// 等待 up 或取消
waitForUpOrCancellation(): PointerInputChange?
waitForUpOrCancellation(pass: PointerEventPass = PointerEventPass.Main)

// 消费事件
event.consume()  // 标记事件已消费
event.isConsumed  // 检查是否已消费
```

### 相关文件

- `ui/screen/reader/ReaderContent.kt`
