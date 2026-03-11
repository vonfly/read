# 阅读器 (Reader) 第一期 MVP 实现计划

## Context
用户需要阅读已添加到书架的书籍，实现核心阅读功能：文本渲染、翻页、进度保存、控制栏切换。

---

## 验收标准（来自 reader-spec.md）
- [ ] 从书籍详情页点击"立即阅读"跳转到阅读器页面
- [ ] 正确渲染书籍内容（段落文本）
- [ ] 左右滑动或点击屏幕左右区域可翻页
- [ ] 顶部显示章节标题，底部显示阅读进度（当前页/总页数）
- [ ] 点击屏幕中央显示/隐藏控制栏（TopBar + BottomBar）
- [ ] 阅读进度自动保存到本地，下次打开恢复到上次位置
- [ ] 返回按钮正常工作，返回书籍详情页

---

## 设计稿页面映射
| 设计稿页面 | 功能 | 本期实现 |
|-----------|------|---------|
| Read | 基础阅读页面 | ✅ |
| Read-Control-Bar | 带控制栏的阅读 | ✅ |

---

## 实现任务（按依赖关系分批）

### 第一批：基础设施（无依赖，可并行）
1. `di/qualifier/DataStoreQualifiers.kt` - 创建 @AppDataStore 和 @ReaderDataStore 限定符
2. `di/DataStoreModule.kt` - 提供两个 DataStore 实例
3. `ui/theme/ReaderTheme.kt` - 阅读器主题（CompositionLocal）

### 第二批：Domain 层（依赖第一批）
4. `domain/model/PageContent.kt` - 分页内容模型
5. `domain/model/ReadingProgress.kt` - 阅读进度模型
6. `domain/repository/ReadingProgressRepository.kt` - 进度仓库接口

### 第三批：Data 层（依赖第二批）
7. `data/local/entity/ReadingProgressEntity.kt` - Room Entity
8. `data/local/dao/ReadingProgressDao.kt` - DAO 接口
9. `data/repository/ReadingProgressRepositoryImpl.kt` - 进度仓库实现
10. `data/local/AppDatabase.kt` - 添加新 Entity 和 DAO

### 第四批：UseCase（依赖第三批）
11. `domain/usecase/GetBookContentUseCase.kt` - 获取书籍内容（Mock）
12. `domain/usecase/GetReadingProgressUseCase.kt` - 获取阅读进度
13. `domain/usecase/SaveReadingProgressUseCase.kt` - 保存阅读进度
14. `di/RepositoryModule.kt` - 绑定 ReadingProgressRepository

### 第五批：UI 层（依赖第四批）
15. `ui/screen/reader/ReaderViewModel.kt` - ViewModel
16. `ui/screen/reader/ReaderScreen.kt` - 有状态层
17. `ui/screen/reader/ReaderContent.kt` - 无状态层（阅读内容）
18. `ui/screen/reader/components/ReaderTopBar.kt` - 顶部控制栏
19. `ui/screen/reader/components/ReaderBottomBar.kt` - 底部控制栏
20. `ui/screen/reader/components/TapZoneOverlay.kt` - 点击区域覆盖层

### 第六批：Navigation + 集成（依赖第五批）
21. `ui/navigation/AppNavHost.kt` - 启用 ReaderRoute 路由
22. `ui/screen/bookdetail/BookDetailViewModel.kt` - 修复 onNavigateToReader 调用

---

## 数据模型

```kotlin
// domain/model/PageContent.kt
data class PageContent(
    val pageIndex: Int,
    val paragraphs: List<String>,
    val chapterTitle: String? = null
)

// domain/model/ReadingProgress.kt
data class ReadingProgress(
    val bookId: String,
    val pageIndex: Int,
    val positionInPage: Float = 0f,
    val lastReadAt: Long = System.currentTimeMillis()
)

// ui/theme/ReaderTheme.kt
data class ReaderSettings(
    val fontSize: TextUnit = 18.sp,
    val lineHeight: TextUnit = 28.sp,
    val letterSpacing: TextUnit = 0.5.sp,
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default
)

enum class ReaderColorScheme(val background: Color, val text: Color, val secondary: Color) {
    Default(Color(0xFFFAF8F5), Color(0xFF1A1A1A), Color(0xFF666666)),
    Night  (Color(0xFF1C1C1E), Color(0xFFE5E5EA), Color(0xFF8E8E93)),
    Sepia  (Color(0xFFF4ECD8), Color(0xFF5B4636), Color(0xFF8B7355)),
    Green  (Color(0xFFCCE8CC), Color(0xFF1A3A1A), Color(0xFF4A6A4A)),
    Pink   (Color(0xFFFFE4E6), Color(0xFF4A1A1A), Color(0xFF8B5555))
}
```

---

## ViewModel 状态设计

```kotlin
@Immutable
data class ReaderUiState(
    val isLoading: Boolean = true,
    val bookId: String = "",
    val pages: ImmutableList<PageContent> = persistentListOf(),
    val currentPageIndex: Int = 0,
    val chapterTitle: String? = null,
    val isControlBarVisible: Boolean = false
)

sealed class ReaderUiEvent {
    data class ShowSnackbar(val message: String) : ReaderUiEvent()
    data object NavigateBack : ReaderUiEvent()
}
```

---

## 交互实现

### 点击区域划分
- 左 1/3 区域：上一页
- 右 1/3 区域：下一页
- 中央 1/3 区域：显示/隐藏控制栏

### 翻页实现
- 使用 `HorizontalPager`（Compose Foundation）
- 支持左右滑动手势

### 进度保存策略
- 翻页时防抖 500ms 后写入 Room（避免每次翻页都触发 IO）
- 使用协程直写 Room（前台），不使用 WorkManager（本期无服务端同步）

---

## 关键复用
- 主题颜色: `Color.kt` 中的 `$--foreground`, `$--foreground-secondary`
- 尺寸规范: `Dimensions.Spacing`, `Dimensions.Radius`
- 图标: Lucide icon font (chevron-left, settings, list)
- ImmutableList: `kotlinx.collections.immutable`
- Snackbar 异步处理: `scope.launch { snackbarHostState.showSnackbar(...) }`

---

## 验证步骤
1. `./gradlew assembleDebug` 编译通过
2. 模拟器验证：书籍详情页 → 立即阅读 → 阅读器页面显示
3. UI 验证：文本正确渲染、章节标题显示、进度显示
4. 交互验证：
   - 点击左/右区域翻页
   - 点击中央显示/隐藏控制栏
   - 返回按钮正常工作
5. 进度保存验证：
   - 翻页后返回
   - 重新进入阅读器，恢复到上次位置
