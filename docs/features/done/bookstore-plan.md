# 书城功能实现计划

> 基于 `docs/features/bookstore-spec.md` 生成

## 概述

实现书城（发现页）功能，包含搜索栏、分类标签、Banner 轮播、热门榜单。本期使用 Mock 数据，不涉及真实 API。

---

## Phase 1: Domain 层（4 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 1 | `domain/model/StoreBook.kt` | 书城书籍模型 |
| 2 | `domain/model/Banner.kt` | Banner 模型 |
| 3 | `domain/model/Category.kt` | 分类模型 |
| 4 | `domain/repository/StoreRepository.kt` | 仓库接口 |

### 1.1 StoreBook.kt
```kotlin
data class StoreBook(
    val id: String,
    val rank: Int,
    val title: String,
    val author: String,
    val rating: Float,
    val readCount: String,
    val coverGradientStart: String,
    val coverGradientEnd: String
)
```

### 1.2 Banner.kt
```kotlin
data class Banner(
    val id: String,
    val title: String,
    val description: String,
    val buttonText: String,
    val gradientStart: String,
    val gradientEnd: String
)
```

### 1.3 Category.kt
```kotlin
data class Category(
    val id: String,
    val name: String
)
```

### 1.4 StoreRepository.kt
```kotlin
interface StoreRepository {
    fun getBanners(): Flow<List<Banner>>
    fun getCategories(): Flow<List<Category>>
    fun getHotBooks(categoryId: String?): Flow<List<StoreBook>>
}
```

---

## Phase 2: Data 层（2 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 5 | `data/repository/StoreRepositoryImpl.kt` | 仓库实现（Mock 数据） |
| 6 | `di/RepositoryModule.kt` | 添加 StoreRepository 绑定（修改现有） |

### 2.1 StoreRepositoryImpl.kt
- 返回硬编码 Mock 数据
- Banner：3 张（限时免费、新人专享、会员特惠）
- 分类：推荐、小说、文学、历史、科技
- 热门书籍：每个分类 5 本书

### 2.2 RepositoryModule.kt（修改）
```kotlin
@Binds @Singleton
abstract fun bindStoreRepository(impl: StoreRepositoryImpl): StoreRepository
```

---

## Phase 3: Domain UseCase（2 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 7 | `domain/usecase/GetBannersUseCase.kt` | 获取 Banner 列表 |
| 8 | `domain/usecase/GetHotBooksUseCase.kt` | 获取热门书籍 |

### 3.1 GetBannersUseCase.kt
```kotlin
class GetBannersUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    operator fun invoke(): Flow<List<Banner>> = repository.getBanners()
}
```

### 3.2 GetHotBooksUseCase.kt
```kotlin
class GetHotBooksUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    operator fun invoke(categoryId: String?): Flow<List<StoreBook>> =
        repository.getHotBooks(categoryId)
}
```

---

## Phase 4: UI 组件（4 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 9 | `ui/component/SearchBar.kt` | 搜索栏组件 |
| 10 | `ui/component/CategoryChips.kt` | 分类标签组件 |
| 11 | `ui/component/BannerCard.kt` | Banner 卡片组件 |
| 12 | `ui/component/BookRankItem.kt` | 书籍排行项组件 |

### 4.1 SearchBar.kt
- 高度 40dp，圆角 10dp
- 背景 #F2F2F7
- 左侧搜索图标 + 占位文字

### 4.2 CategoryChips.kt
- 横向滚动 LazyRow
- 标签高度 32dp，圆角 16dp
- 选中/未选中状态切换

### 4.3 BannerCard.kt
- 高度 140dp，圆角 16dp
- 渐变背景 + 标题 + 描述 + 按钮
- 支持点击回调

### 4.4 BookRankItem.kt
- 高度 80dp，圆角 12dp
- 排名 + 封面(48x64dp) + 书籍信息

---

## Phase 5: Screen 层（3 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 13 | `ui/screen/bookstore/StoreViewModel.kt` | ViewModel |
| 14 | `ui/screen/bookstore/StoreScreen.kt` | 有状态层 |
| 15 | `ui/screen/bookstore/StoreContent.kt` | 无状态层 |

### 5.1 StoreViewModel.kt
```kotlin
@Immutable
data class StoreUiState(
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = false
)

sealed class StoreUiEvent {
    data class ShowSnackbar(val message: String) : StoreUiEvent()
    data class NavigateToBookDetail(val bookId: String) : StoreUiEvent()
}

@HiltViewModel
class StoreViewModel @Inject constructor(
    getBannersUseCase: GetBannersUseCase,
    getHotBooksUseCase: GetHotBooksUseCase
) : ViewModel() {
    // 状态管理
}
```

### 5.2 StoreScreen.kt
- 使用 `hiltViewModel()` 获取 ViewModel
- `collectAsStateWithLifecycle()` 收集状态
- `LaunchedEffect` 处理事件

### 5.3 StoreContent.kt
- LazyColumn 布局
- 顺序：SearchBar → CategoryChips → Banner 轮播 → 热门榜单
- 可 Preview

---

## Phase 6: Banner 轮播（1 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 16 | `ui/component/BannerPager.kt` | Banner 轮播容器 |

### 6.1 BannerPager.kt
- 使用 `HorizontalPager`（Compose Foundation）
- 自动轮播间隔 3 秒
- 底部指示器

---

## Phase 7: 导航集成（2 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 17 | `ui/navigation/AppRoutes.kt` | 添加 StoreRoute |
| 18 | `ui/navigation/AppNavHost.kt` | 添加路由配置 |

### 7.1 AppRoutes.kt（修改）
```kotlin
@Serializable object BookListRoute
@Serializable object StoreRoute  // 新增
```

### 7.2 AppNavHost.kt（修改）
```kotlin
composable<StoreRoute> {
    StoreScreen(
        onBookClick = { /* TODO */ },
        onNavigateBack = { navController.popBackStack() }
    )
}
```

---

## Phase 8: TabBar 导航（1 个文件）

| # | 文件路径 | 说明 |
|---|----------|------|
| 19 | `ui/component/TabBar.kt` | 修改点击回调支持导航 |

### 8.1 TabBar.kt（修改）
- 添加 `onTabClick: (Int) -> Unit` 参数
- 点击 Tab 时触发导航回调

---

## 验证清单

- [ ] 页面布局正确：搜索栏 → 分类 → Banner → 热门榜单
- [ ] 分类标签切换正常，数据刷新
- [ ] Banner 轮播自动切换 + 手动滑动
- [ ] 书籍排行项显示正确
- [ ] 底部 TabBar 导航正常
- [ ] 点击事件显示 Toast（搜索、Banner、更多）

---

## 文件总数：19 个

- 新建：16 个
- 修改：3 个（RepositoryModule, AppRoutes, AppNavHost, TabBar）
