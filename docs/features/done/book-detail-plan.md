# Book-Detail 功能实现计划

## Context
用户在书架或书城中点击一本书后，需要查看该书的详细信息（封面、书名、作者、评分、简介、目录），并决定是否加入书架、下载或开始阅读。

## 设计稿分析（Pencil MCP 读取）
- **Header**: 返回按钮(chevron-left) + 分享按钮(share-2), height: 44dp
- **Content** (padding: 20dp, gap: 20dp):
  - BookInfo: 封面(100x140dp, 渐变色, 圆角8dp) + 书名(22sp bold) + 作者(14sp) + 评分(9.4分) + 阅读人数
  - Description: "简介"标题(16sp bold) + 内容(14sp, lineHeight 1.6)
  - Chapters: "目录 · 共256章" + 查看更多箭头 + 章节列表(显示2章, 每项48dp高, 圆角8dp)
- **Footer**: 加入书架(48x48dp) + 下载(48x48dp) + 立即阅读按钮(fill_container, height: 48dp, 圆角24dp)

---

## 实现任务（按依赖关系分批）

### 第一批：Domain Model（无依赖，可并行）
1. `domain/model/BookDetail.kt` - 书籍详情模型
2. `domain/model/Chapter.kt` - 章节模型
3. `domain/repository/BookDetailRepository.kt` - 仓库接口

### 第二批：Data 层（依赖第一批）
4. `data/local/entity/BookDetailEntity.kt` - Room Entity
5. `data/local/entity/ChapterEntity.kt` - Room Entity
6. `data/local/dao/BookDetailDao.kt` - DAO 接口
7. `data/local/dao/ChapterDao.kt` - DAO 接口
8. `data/mapper/BookDetailMapper.kt` - Entity ↔ Domain 扩展函数
9. `data/repository/BookDetailRepositoryImpl.kt` - 仓库实现（本期 Mock 数据）
10. `data/local/AppDatabase.kt` - 添加新 Entity 和 DAO

### 第三批：UseCase + DI（依赖第二批）
11. `domain/usecase/GetBookDetailUseCase.kt` - 获取详情
12. `domain/usecase/AddBookToShelfUseCase.kt` - 加入书架
13. `di/RepositoryModule.kt` - 绑定 BookDetailRepository

### 第四批：UI 层（依赖第三批）
14. `ui/screen/bookdetail/BookDetailViewModel.kt` - ViewModel
15. `ui/screen/bookdetail/BookDetailScreen.kt` - 有状态层
16. `ui/screen/bookdetail/BookDetailContent.kt` - 无状态层
17. `ui/screen/bookdetail/components/BookInfoSection.kt` - 封面+书名+评分组件
18. `ui/screen/bookdetail/components/DescriptionSection.kt` - 简介组件
19. `ui/screen/bookdetail/components/ChapterSection.kt` - 目录组件
20. `ui/screen/bookdetail/components/BottomActionBar.kt` - 底部操作栏

### 第五批：Navigation + 集成（依赖第四批）
21. `ui/navigation/AppRoutes.kt` - 添加 BookDetailRoute
22. `ui/navigation/AppNavHost.kt` - 添加路由
23. `ui/screen/booklist/BookListScreen.kt` - 修改点击跳转

---

## 数据模型

```kotlin
// domain/model/BookDetail.kt
data class BookDetail(
    val id: String,
    val title: String,
    val author: String,
    val coverGradientStart: String,
    val coverGradientEnd: String,
    val rating: Float,
    val readerCount: Int,
    val description: String,
    val totalChapters: Int,
    val isInShelf: Boolean,
    val chapters: ImmutableList<Chapter>
)

// domain/model/Chapter.kt
data class Chapter(
    val id: String,
    val bookId: String,
    val title: String,
    val index: Int,
    val isFree: Boolean
)
```

---

## ViewModel 状态设计

```kotlin
@Immutable
data class BookDetailUiState(
    val isLoading: Boolean = true,
    val bookDetail: BookDetail? = null,
    val isDescriptionExpanded: Boolean = false
)

sealed class BookDetailUiEvent {
    data class ShowSnackbar(val message: String) : BookDetailUiEvent()
    data object NavigateBack : BookDetailUiEvent()
    data class NavigateToReader(val bookId: String) : BookDetailUiEvent()
    data class NavigateToContents(val bookId: String) : BookDetailUiEvent()
}
```

---

## 关键复用
- 主题颜色: `$--foreground`, `$--foreground-secondary`, `$--primary`, `$--card`, `$--success`
- 尺寸规范: `Dimensions.Spacing`, `Dimensions.Radius`
- 图标: Lucide icon font (chevron-left, share-2, star, download, plus)
- ImmutableList: `kotlinx.collections.immutable`

---

## 验证步骤
1. `./gradlew assembleDebug` 编译通过
2. 模拟器验证：书架点击 → 详情页显示
3. UI 对比设计稿：封面渐变、评分星标、章节列表
4. 交互验证：立即阅读跳转、加入书架状态切换
5. Toast 验证：下载、分享、章节点击
