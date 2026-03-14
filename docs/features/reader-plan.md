# 阅读器页面（Reader）- 实现计划

## 状态概览
- 创建时间：2026-03-14
- 进度：18/18 (100%)

---

## Context

用户需要一个沉浸式阅读界面，支持 EPUB/TXT 格式书籍阅读，提供翻页、进度追踪、控制栏显示/隐藏等核心功能。

**关键决策：**
- 本期使用 **Mock 内容**（已有 `GetBookContentUseCase`）
- 阅读进度 **Room 持久化**（已有 `ReadingProgressEntity`）
- 控制栏点击屏幕中央显示/隐藏
- 翻页使用 **LazyColumn + 手势**（本期不做动画）
- 所有设置按钮仅 **Toast 提示**（下期实现面板）

---

## 第一批：Domain 层（可并行）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/ReaderSettings.kt` | 阅读器设置数据类（字体、行距、主题） |
| ✅ | `domain/repository/ReaderPreferencesRepository.kt` | 阅读器偏好 Repository 接口 |

> 注：`PageContent.kt`、`ReadingProgress.kt`、`GetBookContentUseCase.kt`、`ReadingProgressRepository.kt` 已存在

---

## 第二批：Data 层（依赖第一批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `di/qualifier/DataStoreQualifiers.kt` | 添加 `@ReaderDataStore` 注解 |
| ✅ | `di/DataStoreModule.kt` | 添加 `@ReaderDataStore` provider |
| ✅ | `data/local/preferences/ReaderPreferencesRepositoryImpl.kt` | 阅读器偏好 Repository 实现 |
| ✅ | `di/RepositoryModule.kt` | 添加 ReaderPreferencesRepository 的 Hilt 绑定 |

---

## 第三批：UI 主题系统（依赖第二批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/theme/ReaderTheme.kt` | ReaderSettings + CompositionLocalProvider |
| ✅ | `ui/theme/Color.kt` | 添加 SliderActive (#C5C5CA) 颜色 |

---

## 第四批：UI 层 Reader 页面（依赖第三批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/reader/ReaderViewModel.kt` | ViewModel（UiState + UiEvent + 进度保存） |
| ✅ | `ui/screen/reader/ReaderScreen.kt` | 有状态 Screen（沉浸式 + 事件处理） |
| ✅ | `ui/screen/reader/ReaderContent.kt` | 无状态 Content（可 Preview） |
| ✅ | `ui/screen/reader/components/ReaderTopBar.kt` | 顶部控制栏组件 |
| ✅ | `ui/screen/reader/components/ReaderBottomBar.kt` | 底部控制栏组件 |
| ✅ | `ui/screen/reader/components/ReaderFooter.kt` | 底部状态栏（时间 + 页码） |

---

## 第五批：导航集成（依赖第四批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/navigation/AppRoutes.kt` | 添加 ReaderRoute(bookId: String) |
| ✅ | `ui/navigation/AppNavHost.kt` | 添加 Reader 页面路由 |
| ✅ | `ui/screen/bookdetail/BookDetailScreen.kt` | 添加 onNavigateToReader 参数 |
| ✅ | `ui/screen/bookdetail/BookDetailContent.kt` | 传递导航回调（无需修改，通过 lambda 传递） |

---

## 关键代码结构

### Domain Model
```kotlin
// domain/model/ReaderSettings.kt
data class ReaderSettings(
    val fontSize: TextUnit = 18.sp,
    val lineHeight: Float = 1.8f,
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default
)

enum class ReaderColorScheme(val background: Color, val text: Color) {
    Default(Color(0xFFF9F9F7), Color(0xFF333333)),
    Night(Color(0xFF1C1C1E), Color(0xFFE5E5EA)),
    Sepia(Color(0xFFF4ECD8), Color(0xFF5B4636)),
    Green(Color(0xFFCCE8CC), Color(0xFF1A3A1A))
}
```

### UiState + UiEvent
```kotlin
@Immutable
data class ReaderUiState(
    val isLoading: Boolean = false,
    val bookTitle: String = "",
    val chapterTitle: String = "",
    val pages: ImmutableList<PageContent> = persistentListOf(),
    val currentPageIndex: Int = 0,
    val totalPages: Int = 0,
    val isControlsVisible: Boolean = false
)

sealed class ReaderUiEvent {
    data class ShowSnackbar(val message: String) : ReaderUiEvent()
    data object NavigateBack : ReaderUiEvent()
}
```

### 导航路由
```kotlin
// AppRoutes.kt 新增
@Serializable data class ReaderRoute(val bookId: String)

// AppNavHost.kt 新增 composable
composable<ReaderRoute> {
    val args = it.toRoute<ReaderRoute>()
    ReaderScreen(
        bookId = args.bookId,
        onNavigateBack = { navController.popBackStack() }
    )
}
```

---

## 复用组件/代码

| 组件 | 路径 | 说明 |
|------|------|------|
| PageContent | `domain/model/PageContent.kt` | 已存在，直接使用 |
| GetBookContentUseCase | `domain/usecase/GetBookContentUseCase.kt` | 已存在，返回 Mock 内容 |
| ReadingProgressRepository | `domain/repository/ReadingProgressRepository.kt` | 已存在，用于进度保存 |
| Background | `ui/theme/Color.kt` | #F9F9F7 |
| ForegroundSecondary | `ui/theme/Color.kt` | #6B6B6B |
| ForegroundTertiary | `ui/theme/Color.kt` | #999999 |
| Accent | `ui/theme/Color.kt` | #FF6B4A |

---

## 验证步骤

1. **编译验证**：每批完成后运行 `./gradlew assembleDebug`
2. **UI 验收**：
   - 打开模拟器，从书籍详情页进入阅读器
   - 检查 UI 还原度（对照设计稿 `Read` 和 `Read-Control-Bar` 页面）
   - 点击屏幕中央，验证控制栏显示/隐藏
   - 点击返回按钮，验证返回详情页
   - 点击所有设置按钮，验证 Toast 提示
   - 滑动翻页，验证页面切换
   - 验证时间实时更新、页码正确显示

---

## 注意事项

1. **控制栏显示逻辑**：点击屏幕中央区域（非边缘）显示/隐藏
2. **进度保存防抖**：停止翻页 500ms 后写入 Room
3. **Snackbar 事件**：使用 `scope.launch` 异步显示
4. **段落行高**：Compose `lineHeight = fontSize * lineHeightMultiplier`，设计稿 1.8 对应 `18.sp * 1.3f`（Compose 内部已乘 fontSize）
5. **Mock 数据**：本期使用 `GetBookContentUseCase` 的 Mock 内容
6. **本期不做**：翻页动画、目录面板、字体/亮度/更多设置面板

---

## 实现对照表（Agent 必须严格遵循）

> ⚠️ 以下数值必须精确实现，禁止使用"习惯值"或主题变量默认值
> 完整对照表见 `docs/features/reader-spec.md`

### ReadingLayer - Header
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| Header 高度 | 72 dp | `.height(72.dp)` |
| Header 内边距 | top: 28 dp, horizontal: 20 dp | `.padding(top = 28.dp, start = 20.dp, end = 20.dp)` |
| ChapterTitle 字号 | 14 sp | `fontSize = 14.sp` |
| ChapterTitle 字重 | 500 (Medium) | `fontWeight = FontWeight.Medium` |
| ChapterTitle 颜色 | $--foreground-secondary | `ForegroundSecondary` |

### ReadingLayer - Content
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| 段落间距 | 20 dp | `verticalArrangement = Arrangement.spacedBy(20.dp)` |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` |
| 段落字号 | 18 sp | `fontSize = 18.sp` |
| 段落行高 | 1.8 | `lineHeight = 18.sp * 1.3f` |
| 段落颜色 | #333333 | `Color(0xFF333333)` |

### ReadingLayer - Footer
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| Footer 高度 | 24 dp | `.height(24.dp)` |
| 水平内边距 | 24 dp | `.padding(horizontal = 24.dp)` |
| 底部内边距 | 8 dp | `.padding(bottom = 8.dp)` |
| 时间/页码字号 | 12 sp | `fontSize = 12.sp` |
| 时间/页码颜色 | $--foreground-tertiary | `ForegroundTertiary` |

### TopBar
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| TopBar 高度 | 72 dp | `.height(72.dp)` |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` |
| 背景 | #F9F9F9CC (80% alpha) | `Color(0xFFF9F9F9).copy(alpha = 0.8f)` |
| 返回按钮尺寸 | 24×24 dp | `.size(24.dp)` |
| 书名字号 | 17 sp | `fontSize = 17.sp` |
| 书名字重 | 600 (SemiBold) | `fontWeight = FontWeight.SemiBold` |
| 操作按钮间距 | 24 dp | `Arrangement.spacedBy(24.dp)` |
| 操作按钮图标尺寸 | 22×22 dp | `.size(22.dp)` |
| 加入书架图标颜色 | $--accent | `Accent` |
| 其他按钮图标颜色 | $--foreground-secondary | `ForegroundSecondary` |

### BottomBar
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| BottomBar 高度 | 122 dp | `.height(122.dp)` |
| 顶部圆角 | 16 dp | `RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)` |
| 背景 | #F9F9F9FF | `Color(0xFFF9F9F9)` |
| 顶部内边距 | 16 dp | `.padding(top = 16.dp)` |
| 顶部边框 | 1 dp, #E8E8E8 | `Modifier.border(1.dp, Color(0xFFE8E8E8), RectangleShape)` |

### BottomBar - ProgressBar
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| ProgressBar 高度 | 40 dp | `.height(40.dp)` |
| 间距 | 12 dp | `Arrangement.spacedBy(12.dp)` |
| 翻页按钮尺寸 | 40×40 dp | `.size(40.dp)` |
| 翻页按钮圆角 | 20 dp | `RoundedCornerShape(20.dp)` |
| 翻页图标尺寸 | 24×24 dp | `.size(24.dp)` |
| 进度条高度 | 28 dp | `.height(28.dp)` |
| 进度条圆角 | 14 dp | `RoundedCornerShape(14.dp)` |
| 进度条已读颜色 | $--slider-active (#C5C5CA) | `SliderActive` |

### BottomBar - BottomControls
| 属性 | 设计值 | Compose 实现 |
|------|--------|--------------|
| 按钮组高度 | 66 dp | `.height(66.dp)` |
| 水平内边距 | 20 dp | `.padding(horizontal = 20.dp)` |
| 按钮内间距 | 4 dp | `Arrangement.spacedBy(4.dp)` |
| 图标尺寸 | 22×22 dp | `.size(22.dp)` |
| 标签字号 | 10 sp | `fontSize = 10.sp` |
| 标签颜色 | $--foreground-tertiary | `ForegroundTertiary` |
| 选中时图标颜色 | $--accent | `Accent` |
| 选中时标签颜色 | $--accent | `Accent` |
