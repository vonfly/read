# 书签页面 - 实现计划

## 状态概览
- 创建时间：2026-03-14
- 进度：11/11 (100%)

---

## Context

书签页面用于展示和管理用户阅读过程中添加的书签，支持快速定位到感兴趣的阅读位置。

**关键决策：**
- 本期统计数据使用 **Mock 数据**（避免数据库迁移）
- 阴影效果复用 Profile 页面的 `CardShadowColor`（7% 不透明度）
- 点击书签卡片本期显示 Toast（阅读器功能后续实现）

---

## 第一批：Domain 层模型（可并行）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/Bookmark.kt` | 书签数据类（bookId, title, author, chapter, preview, coverGradient, createdAt） |

---

## 第二批：Data 层（依赖第一批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/repository/BookmarkRepository.kt` | 书签 Repository 接口 |
| ✅ | `data/repository/BookmarkRepositoryImpl.kt` | 书签 Repository 实现（Mock 3 条数据） |
| ✅ | `di/RepositoryModule.kt` | 添加 BookmarkRepository 的 Hilt 绑定 |

---

## 第三批：Domain 层 UseCase（依赖第二批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/usecase/GetBookmarksUseCase.kt` | 获取书签列表（返回 Mock Flow） |

---

## 第四批：UI 层书签页面（依赖第三批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/bookmark/BookmarkViewModel.kt` | ViewModel（UiState + UiEvent） |
| ✅ | `ui/screen/bookmark/BookmarkScreen.kt` | 有状态 Screen（Hilt 注入 ViewModel） |
| ✅ | `ui/screen/bookmark/BookmarkContent.kt` | 无状态 Content（可 Preview） |

---

## 第五批：导航集成（依赖第四批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/navigation/AppRoutes.kt` | 添加 BookmarkRoute |
| ✅ | `ui/navigation/AppNavHost.kt` | 添加书签页面路由 |
| ✅ | `ui/screen/profile/ProfileViewModel.kt` | 更新 onBookmarksClick 发送导航事件 |
| ✅ | `ui/screen/profile/ProfileScreen.kt` | 处理 NavigateToBookmarks 事件 |

---

## 关键代码结构

### Domain Model
```kotlin
// domain/model/Bookmark.kt
data class Bookmark(
    val id: String,
    val bookId: String,
    val title: String,
    val author: String,
    val chapter: Int,
    val preview: String,
    val coverGradient: List<Color>,
    val createdAt: Long
)
```

### BookmarkRepository
```kotlin
// domain/repository/BookmarkRepository.kt
interface BookmarkRepository {
    fun observeAll(): Flow<List<Bookmark>>
}

// data/repository/BookmarkRepositoryImpl.kt
class BookmarkRepositoryImpl @Inject constructor() : BookmarkRepository {
    override fun observeAll(): Flow<List<Bookmark>> = flow {
        emit(
            listOf(
                Bookmark(
                    id = "1",
                    bookId = "book_1",
                    title = "三体",
                    author = "刘慈欣",
                    chapter = 12,
                    preview = "汪淼看着那个幽灵倒计时，时间在不停地闪烁...",
                    coverGradient = listOf(Color(0xFF11998e), Color(0xFF38ef7d)),
                    createdAt = System.currentTimeMillis() - 86400000
                ),
                // ... 更多 Mock 数据
            )
        )
    }
}
```

### BookmarkViewModel 结构
```kotlin
@Immutable
data class BookmarkUiState(
    val isLoading: Boolean = false,
    val bookmarks: ImmutableList<Bookmark> = persistentListOf()
)

sealed class BookmarkUiEvent {
    data class ShowSnackbar(val message: String) : BookmarkUiEvent()
    data object NavigateBack : BookmarkUiEvent()
    data class NavigateToReader(val bookId: String, val bookmarkId: String) : BookmarkUiEvent()
}

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    getBookmarksUseCase: GetBookmarksUseCase
) : ViewModel() {
    // ... 实现
}
```

### 导航改造
```kotlin
// AppRoutes.kt 新增
@Serializable object BookmarkRoute

// AppNavHost.kt 新增 composable
composable<BookmarkRoute> {
    BookmarkScreen(onNavigateBack = { navController.popBackStack() })
}

// ProfileScreen.kt 处理导航事件
is ProfileUiEvent.NavigateToBookmarks -> navController.navigate(BookmarkRoute)
```

---

## 复用组件

| 组件 | 路径 | 说明 |
|------|------|------|
| CardShadowColor | `ui/screen/profile/ProfileContent.kt` | 阴影颜色常量，可提取到 theme 包复用 |
| TabBar | `ui/component/TabBar.kt` | 本页面不需要（是二级页面） |

---

## 验证步骤

1. **编译验证**：每批完成后运行 `./gradlew assembleDebug`
2. **UI 验证**：
   - 打开模拟器，从"我的"页面点击"我的书签"
   - 检查 UI 还原度（对照设计稿）
   - 点击书签卡片，验证 Toast 提示
   - 点击返回按钮，验证导航正确
   - 检查卡片阴影效果

---

## 注意事项

1. **封面渐变**：使用 `Brush.linearGradient` 实现，颜色从 Mock 数据中获取
2. **时间格式**：使用 `SimpleDateFormat("yyyy-MM-dd HH:mm")` 格式化
3. **阴影复用**：考虑将 `CardShadowColor` 提取到 `ui/theme/` 目录供全局复用
4. **空状态**：暂不实现（Mock 数据始终有 3 条）
