package com.vonfly.read.ui.screen.reader

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.Chapter
import com.vonfly.read.domain.model.PageContent
import com.vonfly.read.domain.model.ReaderSettings
import com.vonfly.read.domain.model.ReadingProgress
import com.vonfly.read.domain.repository.ReaderPreferencesRepository
import com.vonfly.read.domain.repository.ReadingProgressRepository
import com.vonfly.read.domain.usecase.GetBookContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ReaderUiState(
    val isLoading: Boolean = false,
    val bookTitle: String = "",
    val chapterTitle: String = "",
    val pages: ImmutableList<PageContent> = persistentListOf(),
    val currentPageIndex: Int = 0,
    val totalPages: Int = 0,
    val isControlsVisible: Boolean = false,
    val readerSettings: ReaderSettings = ReaderSettings(),
    val isInShelf: Boolean = false,
    val isBookmarked: Boolean = false,
    // 目录面板相关
    val chapters: ImmutableList<Chapter> = persistentListOf(),
    val currentChapterIndex: Int = 0,
    val visiblePanel: ReaderPanel? = null,
    val isSortAscending: Boolean = true
)

sealed class ReaderUiEvent {
    data class ShowSnackbar(val message: String) : ReaderUiEvent()
    data object NavigateBack : ReaderUiEvent()
}

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val getBookContentUseCase: GetBookContentUseCase,
    private val readingProgressRepository: ReadingProgressRepository,
    private val readerPreferencesRepository: ReaderPreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: String = savedStateHandle["bookId"] ?: ""

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    private val _event = Channel<ReaderUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private var saveProgressJob: Job? = null

    init {
        loadContent()
        observeReaderSettings()
        loadMockChapters()
    }

    private fun loadMockChapters() {
        // Mock 章节数据（后续替换为真实数据）
        val mockChapters = persistentListOf(
            Chapter(id = "1", bookId = bookId, title = "第1章 科学边界(一)", index = 0, isFree = true),
            Chapter(id = "2", bookId = bookId, title = "第2章 科学边界(二)", index = 1, isFree = true),
            Chapter(id = "3", bookId = bookId, title = "第3章 科学边界(三)", index = 2, isFree = true),
            Chapter(id = "4", bookId = bookId, title = "第4章 红岸工程(一)", index = 3, isFree = false),
            Chapter(id = "5", bookId = bookId, title = "第5章 红岸工程(二)", index = 4, isFree = false),
            Chapter(id = "6", bookId = bookId, title = "第6章 红岸工程(三)", index = 5, isFree = false),
            Chapter(id = "7", bookId = bookId, title = "第7章 三体问题(一)", index = 6, isFree = false),
            Chapter(id = "8", bookId = bookId, title = "第8章 三体问题(二)", index = 7, isFree = false),
            Chapter(id = "9", bookId = bookId, title = "第9章 三体问题(三)", index = 8, isFree = false),
            Chapter(id = "10", bookId = bookId, title = "第10章 地球往事(一)", index = 9, isFree = false),
            Chapter(id = "11", bookId = bookId, title = "第11章 地球往事(二)", index = 10, isFree = false),
            Chapter(id = "12", bookId = bookId, title = "第12章 地球往事(三)", index = 11, isFree = false),
            Chapter(id = "13", bookId = bookId, title = "第13章 宇宙闪烁", index = 12, isFree = false),
            Chapter(id = "14", bookId = bookId, title = "第14章 智子", index = 13, isFree = false),
            Chapter(id = "15", bookId = bookId, title = "第15章 黑暗森林", index = 14, isFree = false)
        )
        _uiState.update { it.copy(chapters = mockChapters) }
    }

    private fun loadContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 获取阅读进度
            val progress = readingProgressRepository.getProgress(bookId)
            val savedPageIndex = progress?.pageIndex ?: 0

            // 获取书籍内容
            getBookContentUseCase(bookId)
                .onSuccess { pages ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            pages = pages,
                            totalPages = pages.size,
                            currentPageIndex = savedPageIndex.coerceIn(0, pages.size - 1),
                            chapterTitle = pages.getOrNull(savedPageIndex)?.chapterTitle ?: ""
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.send(ReaderUiEvent.ShowSnackbar(error.message ?: "加载失败"))
                }
        }
    }

    private fun observeReaderSettings() {
        viewModelScope.launch {
            readerPreferencesRepository.observeSettings().collect { settings ->
                _uiState.update { it.copy(readerSettings = settings) }
            }
        }
    }

    fun onPageChanged(newPageIndex: Int) {
        val pages = _uiState.value.pages
        if (newPageIndex < 0 || newPageIndex >= pages.size) return

        _uiState.update { state ->
            state.copy(
                currentPageIndex = newPageIndex,
                chapterTitle = pages[newPageIndex].chapterTitle ?: state.chapterTitle
            )
        }

        // 防抖保存进度：停止翻页 500ms 后写入 Room
        saveProgressJob?.cancel()
        saveProgressJob = viewModelScope.launch {
            delay(500)
            saveProgress(newPageIndex)
        }
    }

    private suspend fun saveProgress(pageIndex: Int) {
        readingProgressRepository.saveLocalProgress(
            ReadingProgress(
                bookId = bookId,
                pageIndex = pageIndex,
                positionInPage = 0f
            )
        )
    }

    fun toggleControlsVisibility() {
        _uiState.update { it.copy(isControlsVisible = !it.isControlsVisible) }
    }

    fun hideControls() {
        _uiState.update { it.copy(isControlsVisible = false) }
    }

    fun onNavigateBack() {
        viewModelScope.launch {
            // 保存进度后返回
            saveProgress(_uiState.value.currentPageIndex)
            _event.send(ReaderUiEvent.NavigateBack)
        }
    }

    fun onPreviousPage() {
        val currentIndex = _uiState.value.currentPageIndex
        if (currentIndex > 0) {
            onPageChanged(currentIndex - 1)
        }
    }

    fun onNextPage() {
        val currentIndex = _uiState.value.currentPageIndex
        if (currentIndex < _uiState.value.totalPages - 1) {
            onPageChanged(currentIndex + 1)
        }
    }

    fun onProgressChange(progress: Float) {
        val newPageIndex = (progress * (_uiState.value.totalPages - 1)).toInt()
        onPageChanged(newPageIndex)
    }

    // 以下为设置按钮回调
    fun onCatalogClick() {
        _uiState.update { state ->
            state.copy(
                visiblePanel = if (state.visiblePanel == ReaderPanel.CATALOG) null else ReaderPanel.CATALOG
            )
        }
    }

    fun onFontClick() {
        viewModelScope.launch {
            _event.send(ReaderUiEvent.ShowSnackbar("字体设置开发中"))
        }
    }

    fun onBrightnessClick() {
        viewModelScope.launch {
            _event.send(ReaderUiEvent.ShowSnackbar("亮度设置开发中"))
        }
    }

    fun onMoreClick() {
        viewModelScope.launch {
            _event.send(ReaderUiEvent.ShowSnackbar("更多设置开发中"))
        }
    }

    // 目录面板操作
    fun toggleSortOrder() {
        _uiState.update { it.copy(isSortAscending = !it.isSortAscending) }
    }

    fun onChapterClick(chapterIndex: Int) {
        // 计算该章节的起始页索引
        val chapters = _uiState.value.chapters
        if (chapterIndex in chapters.indices) {
            // 简化处理：跳转到对应章节的第一页（实际需要根据章节-页映射计算）
            onPageChanged(chapterIndex)
            _uiState.update { state ->
                state.copy(
                    currentChapterIndex = chapterIndex,
                    visiblePanel = null
                )
            }
        }
    }

    fun hidePanel() {
        _uiState.update { it.copy(visiblePanel = null) }
    }

    // TopBar 操作按钮
    fun onAddToShelfClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInShelf = !it.isInShelf) }
            val message = if (_uiState.value.isInShelf) "已加入书架" else "已从书架移除"
            _event.send(ReaderUiEvent.ShowSnackbar(message))
        }
    }

    fun onBookmarkClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBookmarked = !it.isBookmarked) }
            val message = if (_uiState.value.isBookmarked) "已添加书签" else "已移除书签"
            _event.send(ReaderUiEvent.ShowSnackbar(message))
        }
    }

    fun onSettingsClick() {
        viewModelScope.launch {
            _event.send(ReaderUiEvent.ShowSnackbar("设置功能开发中"))
        }
    }
}
