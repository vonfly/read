# 书架页面实现计划

## 概述

根据 `docs/features/booklist-spec.md` 实现书架首页，包含：
- 继续阅读区块（单本书 + 进度）
- 书架网格列表
- 底部 TabBar 导航

## 现有代码分析

| 文件 | 状态 | 说明 |
|------|------|------|
| `MyApp.kt` | ✅ 已存在 | Hilt Application |
| `MainActivity.kt` | ✅ 已存在 | 入口 Activity |
| `AppTheme.kt` | ⚠️ 需扩展 | 仅默认 Material3，缺少颜色定义 |
| `AppNavHost.kt` | ⚠️ 需修改 | 占位代码，需替换为 BookListScreen |

## 文件实现顺序

> 原则：domain → data → di → ui → navigation，从底层到顶层

---

### Phase 1: 设计系统 (Theme)

#### 1.1 `ui/theme/Color.kt`
**职责**：定义设计稿中的颜色常量

```kotlin
// 从设计稿提取的颜色
val Primary = Color(0xFF007AFF)
val Background = Color(0xFFF9F9F7)
val Card = Color(0xFFFFFFFF)
val Foreground = Color(0xFF1A1A1A)
val ForegroundSecondary = Color(0xFF6B6B6B)
val ForegroundTertiary = Color(0xFF999999)
val BorderLight = Color(0xFFF0F0F0)
val SliderTrack = Color(0xFFE5E5EA)
val SliderActive = Color(0xFFC5C5CA)
```

#### 1.2 `ui/theme/Dimensions.kt`
**职责**：定义间距和圆角常量

```kotlin
object Spacing {
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Lg = 16.dp
    val Xl = 24.dp
}

object Radius {
    val Sm = 8.dp
    val Md = 12.dp
    val Lg = 16.dp
}
```

#### 1.3 `ui/theme/AppTheme.kt`
**职责**：更新主题，应用自定义颜色

---

### Phase 2: Domain 层

#### 2.1 `domain/model/Book.kt`
**职责**：书籍领域模型

```kotlin
data class Book(
    val id: String,
    val title: String,
    val coverGradient: List<String>,  // 渐变色 ["#667eea", "#764ba2"]
    val lastReadAt: Long?,
    val readingProgress: Float,  // 0.0 - 1.0
    val currentChapter: String?
)
```

#### 2.2 `domain/repository/BookRepository.kt`
**职责**：书籍仓库接口

```kotlin
interface BookRepository {
    fun observeAllBooks(): Flow<List<Book>>
    fun observeRecentBook(): Flow<Book?>
    suspend fun deleteBook(id: String): Result<Unit>
}
```

#### 2.3 `domain/usecase/GetBookListUseCase.kt`
**职责**：获取书架列表

#### 2.4 `domain/usecase/GetRecentBookUseCase.kt`
**职责**：获取最近阅读的书籍

#### 2.5 `domain/usecase/DeleteBookUseCase.kt`
**职责**：删除书籍

---

### Phase 3: Data 层

#### 3.1 `data/local/entity/BookEntity.kt`
**职责**：Room 实体

```kotlin
@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val coverGradientStart: String,
    val coverGradientEnd: String,
    val lastReadAt: Long?,
    val readingProgress: Float,
    val currentChapter: String?,
    val createdAt: Long,
    val updatedAt: Long
)
```

#### 3.2 `data/local/dao/BookDao.kt`
**职责**：Room DAO

```kotlin
@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE lastReadAt IS NOT NULL ORDER BY lastReadAt DESC LIMIT 1")
    fun observeRecent(): Flow<BookEntity?>

    @Upsert suspend fun upsert(entity: BookEntity)
    @Delete suspend fun delete(entity: BookEntity)
}
```

#### 3.3 `data/local/AppDatabase.kt`
**职责**：Room 数据库

#### 3.4 `data/mapper/BookMapper.kt`
**职责**：Entity ↔ Domain 转换

#### 3.5 `data/repository/BookRepositoryImpl.kt`
**职责**：仓库实现

---

### Phase 4: DI 层

#### 4.1 `di/DatabaseModule.kt`
**职责**：提供 Database 和 DAO

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase

    @Provides
    fun provideBookDao(db: AppDatabase): BookDao
}
```

#### 4.2 `di/RepositoryModule.kt`
**职责**：绑定 Repository 接口

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository
}
```

---

### Phase 5: UI 层 - 组件

#### 5.1 `ui/component/BookCover.kt`
**职责**：书籍封面组件（渐变背景 + 阴影）

- 尺寸：100×140dp
- 圆角：8dp
- 阴影：`0 4 12 #00000015`
- 渐变方向：135°

#### 5.2 `ui/component/SectionHeader.kt`
**职责**：区块标题组件

- 标题：18sp, Semibold, #1A1A1A
- "更多"：14sp, #6B6B6B
- 布局：space-between

#### 5.3 `ui/component/TabBar.kt`
**职责**：底部导航栏

- 高度：83dp（含安全区）
- 3 个 Tab：书架、书城、我的
- 选中色：#007AFF
- 未选中色：#999999

---

### Phase 6: UI 层 - Screen

#### 6.1 `ui/screen/booklist/BookListViewModel.kt`
**职责**：书架页面 ViewModel

```kotlin
@HiltViewModel
class BookListViewModel @Inject constructor(
    getBookListUseCase: GetBookListUseCase,
    getRecentBookUseCase: GetRecentBookUseCase,
    deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    val books: StateFlow<List<Book>>
    val recentBook: StateFlow<Book?>
    val uiState: StateFlow<BookListUiState>
    val event: Flow<BookListUiEvent>

    fun onBookClick(id: String)
    fun onBookLongPress(id: String)
    fun onDeleteConfirm(id: String)
}
```

#### 6.2 `ui/screen/booklist/BookListScreen.kt`
**职责**：有状态层，连接 ViewModel

#### 6.3 `ui/screen/booklist/BookListContent.kt`
**职责**：无状态层，纯 UI 渲染

包含子组件：
- Header（标题 + 图标）
- ContinueReadingSection
- BookshelfSection
- TabBar

#### 6.4 `ui/screen/booklist/components/ContinueCard.kt`
**职责**：继续阅读卡片

- 高度：100dp
- 圆角：12dp
- 封面：56×76dp
- 进度条

#### 6.5 `ui/screen/booklist/components/BookGrid.kt`
**职责**：书籍网格

- LazyVerticalGrid
- 3 列
- 间距 12dp

#### 6.6 `ui/screen/booklist/components/EmptyState.kt`
**职责**：空状态占位

#### 6.7 `ui/screen/booklist/components/DeleteDialog.kt`
**职责**：删除确认弹窗

---

### Phase 7: Navigation

#### 7.1 `ui/navigation/AppNavHost.kt`
**职责**：更新导航，连接 BookListScreen

```kotlin
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = BookListRoute) {
        composable<BookListRoute> {
            BookListScreen(
                onBookClick = { id -> /* TODO: 跳转阅读器 */ },
                onSearchClick = { /* TODO: 跳转搜索 */ },
                onImportClick = { /* TODO: 跳转导入 */ }
            )
        }
    }
}
```

#### 7.2 `ui/navigation/AppRoutes.kt`
**职责**：路由定义

```kotlin
@Serializable object BookListRoute
@Serializable data class ReaderRoute(val bookId: String)
```

---

### Phase 8: 测试数据

#### 8.1 `data/local/AppDatabase.kt` (回调)
**职责**：在 `onCreate` 中插入测试数据

```kotlin
// 插入 3-5 本测试书籍，验证 UI 显示
```

---

## 文件清单（共 22 个文件）

| # | 文件路径 | 职责 |
|---|----------|------|
| 1 | `ui/theme/Color.kt` | 颜色常量 |
| 2 | `ui/theme/Dimensions.kt` | 间距/圆角常量 |
| 3 | `ui/theme/AppTheme.kt` | 更新主题 |
| 4 | `domain/model/Book.kt` | 领域模型 |
| 5 | `domain/repository/BookRepository.kt` | 仓库接口 |
| 6 | `domain/usecase/GetBookListUseCase.kt` | 获取书架列表 |
| 7 | `domain/usecase/GetRecentBookUseCase.kt` | 获取最近阅读 |
| 8 | `domain/usecase/DeleteBookUseCase.kt` | 删除书籍 |
| 9 | `data/local/entity/BookEntity.kt` | Room 实体 |
| 10 | `data/local/dao/BookDao.kt` | Room DAO |
| 11 | `data/local/AppDatabase.kt` | Room 数据库 |
| 12 | `data/mapper/BookMapper.kt` | Entity↔Domain |
| 13 | `data/repository/BookRepositoryImpl.kt` | 仓库实现 |
| 14 | `di/DatabaseModule.kt` | Database DI |
| 15 | `di/RepositoryModule.kt` | Repository DI |
| 16 | `ui/component/BookCover.kt` | 封面组件 |
| 17 | `ui/component/SectionHeader.kt` | 区块标题 |
| 18 | `ui/component/TabBar.kt` | 底部导航 |
| 19 | `ui/screen/booklist/BookListViewModel.kt` | ViewModel |
| 20 | `ui/screen/booklist/BookListScreen.kt` | 有状态 Screen |
| 21 | `ui/screen/booklist/BookListContent.kt` | 无状态 Content |
| 22 | `ui/screen/booklist/components/ContinueCard.kt` | 继续阅读卡片 |
| 23 | `ui/screen/booklist/components/BookGrid.kt` | 书籍网格 |
| 24 | `ui/screen/booklist/components/EmptyState.kt` | 空状态 |
| 25 | `ui/screen/booklist/components/DeleteDialog.kt` | 删除弹窗 |
| 26 | `ui/navigation/AppRoutes.kt` | 路由定义 |
| 27 | `ui/navigation/AppNavHost.kt` | 更新导航 |

---

## 验收检查点

### 编译验证
每个 Phase 完成后执行 `Build → Make Project`，确保无编译错误。

### 功能验收（全部完成后）
- [ ] 页面背景色为 `#F9F9F7`
- [ ] 顶部标题 "书架" 显示正确，图标对齐
- [ ] 继续阅读卡片显示最近书籍 + 进度
- [ ] 无继续阅读时隐藏整个区块
- [ ] 书架网格显示所有书籍，3 列布局
- [ ] 点击书籍可触发事件（本期不跳转）
- [ ] 长按书籍弹出删除确认 Dialog
- [ ] 删除后书籍从列表移除
- [ ] 空书架显示空状态占位
- [ ] TabBar 显示 3 个 Tab，"书架" 高亮

---

## 预估工作量

| Phase | 文件数 | 复杂度 |
|-------|--------|--------|
| Phase 1: Theme | 3 | 低 |
| Phase 2: Domain | 5 | 低 |
| Phase 3: Data | 5 | 中 |
| Phase 4: DI | 2 | 低 |
| Phase 5: Components | 3 | 中 |
| Phase 6: Screen | 7 | 高 |
| Phase 7: Navigation | 2 | 低 |
| Phase 8: Test Data | - | 低 |

**建议执行节奏**：每个 Phase 完成后编译验证，再进入下一个 Phase。
