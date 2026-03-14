# 我的页面（Profile）- 实现计划

## 状态概览
- 创建时间：2026-03-14
- 进度：24/24 (100%)

---

## Context

用户需要一个个人中心页面，用于查看阅读统计、管理个人数据和访问常用功能入口。

**关键决策：**
- 本期统计数据使用 **Mock 数据**（避免数据库迁移）
- 夜间模式 **完整实现**（DataStore 持久化 + AppTheme 深色主题）
- TabBar 导航改造，支持三个 Tab 切换

---

## 第一批：Domain 层模型（可并行）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/model/UserProfile.kt` | 用户资料数据类（昵称、ID） |
| ✅ | `domain/model/ReadingStats.kt` | 阅读统计数据类（已读数、小时数、收藏数） |
| ✅ | `domain/repository/ThemeRepository.kt` | 主题 Repository 接口 |
| ✅ | `domain/repository/UserPreferencesRepository.kt` | 用户偏好 Repository 接口 |

---

## 第二批：Data 层实现（依赖第一批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `data/local/preferences/ThemeRepositoryImpl.kt` | 主题 Repository 实现（DataStore 读写 dark_mode） |
| ✅ | `data/local/preferences/UserPreferencesRepositoryImpl.kt` | 用户偏好 Repository 实现（Mock 用户信息） |
| ✅ | `di/RepositoryModule.kt` | 添加新 Repository 的 Hilt 绑定 |

---

## 第三批：Domain 层 UseCase（依赖第二批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `domain/usecase/GetUserProfileUseCase.kt` | 获取用户资料（返回 Mock Flow） |
| ✅ | `domain/usecase/GetReadingStatsUseCase.kt` | 获取阅读统计（返回 Mock Flow） |
| ✅ | `domain/usecase/GetThemeModeUseCase.kt` | 观察主题模式 Flow |
| ✅ | `domain/usecase/SetThemeModeUseCase.kt` | 设置主题模式 |

---

## 第四批：UI 主题系统升级（依赖第二批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/theme/Color.kt` | 添加深色主题颜色变量 |
| ✅ | `ui/theme/AppTheme.kt` | 添加 DarkColorScheme，实现主题切换 |

---

## 第五批：UI 层 Profile 页面（依赖第三、四批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/screen/profile/ProfileViewModel.kt` | ViewModel（UiState + UiEvent） |
| ✅ | `ui/screen/profile/ProfileScreen.kt` | 有状态 Screen（Hilt 注入 ViewModel） |
| ✅ | `ui/screen/profile/ProfileContent.kt` | 无状态 Content（可 Preview） |

---

## 第六批：导航集成（依赖第五批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `ui/navigation/AppRoutes.kt` | 添加 ProfileRoute |
| ✅ | `ui/navigation/AppNavHost.kt` | 添加 Profile 页面路由 |
| ✅ | `ui/screen/booklist/BookListScreen.kt` | 添加 onNavigateToProfile 参数 |
| ✅ | `ui/screen/booklist/BookListContent.kt` | 传递导航回调到 TabBar |
| ✅ | `ui/screen/bookstore/StoreScreen.kt` | 添加 onNavigateToProfile 参数 |
| ✅ | `ui/screen/bookstore/StoreContent.kt` | 传递导航回调到 TabBar |

---

## 第七批：全局主题集成（依赖第四批）

| 状态 | 文件路径 | 职责 |
|------|----------|------|
| ✅ | `MainActivity.kt` | 读取主题状态并应用 AppTheme |

---

## 关键代码结构

### Domain Model
```kotlin
// domain/model/UserProfile.kt
data class UserProfile(val nickname: String, val userId: String)

// domain/model/ReadingStats.kt
data class ReadingStats(val booksRead: Int, val readingHours: Int, val booksFavorited: Int)
```

### ThemeRepository
```kotlin
// domain/repository/ThemeRepository.kt
interface ThemeRepository {
    fun isDarkMode(): Flow<Boolean>
    suspend fun setDarkMode(enabled: Boolean)
}

// data/local/preferences/ThemeRepositoryImpl.kt
class ThemeRepositoryImpl(@AppDataStore private val dataStore: DataStore<Preferences>) : ThemeRepository {
    companion object {
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
    }
    // 实现略
}
```

### ProfileViewModel 结构
```kotlin
@Immutable
data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile = UserProfile("阅读爱好者", "ID: 12345678"),
    val readingStats: ReadingStats = ReadingStats(128, 256, 32),
    val isDarkMode: Boolean = false
)

sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
    data object NavigateToBookmarks : ProfileUiEvent()
    // ...其他事件
}
```

### 导航改造
```kotlin
// AppRoutes.kt 新增
@Serializable object ProfileRoute

// AppNavHost.kt 新增 composable
composable<ProfileRoute> {
    ProfileScreen(
        onNavigateToBookList = { navController.popBackStack(BookListRoute, false) },
        onNavigateToStore = { navController.navigate(StoreRoute) { launchSingleTop = true } }
    )
}
```

---

## 复用组件

| 组件 | 路径 | 说明 |
|------|------|------|
| TabBar | `ui/component/TabBar.kt` | 底部导航栏，Profile 页面 selectedIndex=2 |
| AppTheme | `ui/theme/AppTheme.kt` | 需扩展支持 darkTheme |
| DataStore | `di/DataStoreModule.kt` | 已有 @AppDataStore |

---

## 验证步骤

1. **编译验证**：每批完成后运行 `./gradlew assembleDebug`
2. **UI 验收**：
   - 打开模拟器，导航到 Profile 页面
   - 检查 UI 还原度（对照设计稿）
   - 点击夜间模式开关，验证主题切换
   - 点击各菜单项，验证 Toast 提示
   - 点击 TabBar 各 Tab，验证导航正确

---

## 注意事项

1. **Snackbar 事件处理**：使用 `scope.launch` 异步显示，避免阻塞事件流
2. **TabBar 导航**：使用 `launchSingleTop = true` 避免重复堆叠
3. **DataStore key**：`dark_mode` 布尔值
4. **Mock 数据**：后续可从 Room 查询真实数据替换
