# 模板：Paging 3

适用场景：书架列表、搜索结果、书城推荐等需要分页加载的列表。

## 纯本地分页（Room → PagingSource）

```kotlin
// data/local/dao/BookDao.kt
@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY updatedAt DESC")
    fun pagingSource(): PagingSource<Int, BookEntity>  // Room 自动生成实现
}

// domain/usecase/GetBookListUseCase.kt
class GetBookListUseCase @Inject constructor(
    private val repository: BookRepository
) {
    operator fun invoke(): Flow<PagingData<Book>> = repository.getBooksPaged()
}

// data/repository/BookRepositoryImpl.kt
override fun getBooksPaged(): Flow<PagingData<Book>> =
    Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            prefetchDistance = 5
        ),
        pagingSourceFactory = { bookDao.pagingSource() }
    ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
```

## 网络 + 本地缓存分页（RemoteMediator）

```kotlin
// data/paging/BookRemoteMediator.kt
@OptIn(ExperimentalPagingApi::class)
class BookRemoteMediator @Inject constructor(
    private val api: BookApiService,
    private val db: AppDatabase
) : RemoteMediator<Int, BookEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, BookEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH  -> 1
            LoadType.PREPEND  -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND   -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.page + 1
            }
        }
        return try {
            val response = api.getBooks(page = page, pageSize = state.config.pageSize)
            db.withTransaction {
                if (loadType == LoadType.REFRESH) db.bookDao().clearAll()
                db.bookDao().insertAll(response.map { it.toEntity(page) })
            }
            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            MediatorResult.Error(e)
        }
    }
}
```

## ViewModel

```kotlin
// ui/screen/booklist/BookListViewModel.kt
@HiltViewModel
class BookListViewModel @Inject constructor(
    getBookListUseCase: GetBookListUseCase
) : ViewModel() {

    // PagingData 不放进 UiState，单独暴露 Flow
    // cachedIn 必须加，防止重组时重新请求
    val books: Flow<PagingData<Book>> = getBookListUseCase()
        .cachedIn(viewModelScope)

    // 如有其他 UI 状态（搜索词、筛选条件等）仍使用标准 UiState + UiEvent 双轨
    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private val _event = Channel<BookListUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()
}

@Immutable
data class BookListUiState(
    val isRefreshing: Boolean = false
)

sealed class BookListUiEvent {
    data class ShowSnackbar(val message: String) : BookListUiEvent()
    data class NavigateToReader(val bookId: String) : BookListUiEvent()
}
```

## Screen + Content（三文件分层）

```kotlin
// ui/screen/booklist/BookListScreen.kt — 有状态层，不可 Preview
@Composable
fun BookListScreen(
    onNavigateToReader: (String) -> Unit,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // PagingData 使用 collectAsLazyPagingItems()，它内部已做 Lifecycle 感知
    val books = viewModel.books.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is BookListUiEvent.ShowSnackbar       -> snackbarHostState.showSnackbar(event.message)
                is BookListUiEvent.NavigateToReader   -> onNavigateToReader(event.bookId)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        BookListContent(
            uiState = uiState,
            books = books,
            onBookClick = { bookId -> viewModel.onBookClick(bookId) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// ui/screen/booklist/BookListContent.kt — 无状态层，可 Preview
@Composable
fun BookListContent(
    uiState: BookListUiState,
    books: LazyPagingItems<Book>,
    onBookClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            count = books.itemCount,
            key = books.itemKey { it.id },
            contentType = books.itemContentType { "Book" }
        ) { index ->
            val book = books[index]
            if (book != null) {
                BookCard(book = book, onClick = { onBookClick(book.id) })
            } else {
                BookCardPlaceholder()
            }
        }

        // 加载更多状态
        when (val append = books.loadState.append) {
            is LoadState.Loading -> item { CircularProgressIndicator() }
            is LoadState.Error   -> item {
                Text("加载失败：${append.error.message}")
                Button(onClick = { books.retry() }) { Text("重试") }
            }
            else -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookListContentPreview() {
    AppTheme {
        // Preview 中用空 PagingData 演示空状态
        BookListContent(
            uiState = BookListUiState(),
            books = flowOf(PagingData.empty<Book>()).collectAsLazyPagingItems(),
            onBookClick = {}
        )
    }
}
```
