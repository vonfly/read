package com.vonfly.read.ui.screen.reader

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.PageContent
import com.vonfly.read.domain.model.ReadingProgress
import com.vonfly.read.domain.usecase.GetBookContentUseCase
import com.vonfly.read.domain.usecase.GetReadingProgressUseCase
import com.vonfly.read.domain.usecase.SaveReadingProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val getBookContentUseCase: GetBookContentUseCase,
    private val getReadingProgressUseCase: GetReadingProgressUseCase,
    private val saveReadingProgressUseCase: SaveReadingProgressUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId: String = savedStateHandle["bookId"] ?: ""

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    private val _event = Channel<ReaderUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private var saveJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(bookId = bookId, isLoading = true) }

            // 获取书籍内容
            val contentResult = getBookContentUseCase(bookId)
            if (contentResult.isFailure) {
                _uiState.update { it.copy(isLoading = false) }
                _event.send(ReaderUiEvent.ShowSnackbar("加载书籍内容失败"))
                return@launch
            }

            val pages = contentResult.getOrThrow()

            // 获取阅读进度
            val progress = getReadingProgressUseCase(bookId)
            val initialPageIndex = progress?.pageIndex ?: 0

            _uiState.update {
                it.copy(
                    isLoading = false,
                    pages = pages,
                    currentPageIndex = initialPageIndex.coerceIn(0, pages.size - 1),
                    chapterTitle = pages.getOrNull(initialPageIndex)?.chapterTitle
                )
            }
        }
    }

    /**
     * 页面改变时调用
     *
     * 防抖保存：停止翻页 500ms 后写入 Room，避免每次翻页都触发 IO
     */
    fun onPageChange(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= _uiState.value.pages.size) return

        val page = _uiState.value.pages[pageIndex]
        _uiState.update {
            it.copy(
                currentPageIndex = pageIndex,
                chapterTitle = page.chapterTitle ?: it.chapterTitle
            )
        }

        // 防抖保存进度
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(500)
            saveReadingProgressUseCase(
                ReadingProgress(
                    bookId = bookId,
                    pageIndex = pageIndex,
                    positionInPage = 0f
                )
            )
        }
    }

    /**
     * 切换控制栏显示状态
     */
    fun toggleControlBar() {
        _uiState.update { it.copy(isControlBarVisible = !it.isControlBarVisible) }
    }

    /**
     * 隐藏控制栏
     */
    fun hideControlBar() {
        _uiState.update { it.copy(isControlBarVisible = false) }
    }

    /**
     * 返回上一页
     */
    fun goToPreviousPage(): Boolean {
        val currentIndex = _uiState.value.currentPageIndex
        if (currentIndex > 0) {
            onPageChange(currentIndex - 1)
            return true
        }
        return false
    }

    /**
     * 跳转到下一页
     */
    fun goToNextPage(): Boolean {
        val currentIndex = _uiState.value.currentPageIndex
        if (currentIndex < _uiState.value.pages.size - 1) {
            onPageChange(currentIndex + 1)
            return true
        }
        return false
    }

    /**
     * 返回按钮点击
     */
    fun onBackClick() {
        viewModelScope.launch {
            _event.send(ReaderUiEvent.NavigateBack)
        }
    }
}
