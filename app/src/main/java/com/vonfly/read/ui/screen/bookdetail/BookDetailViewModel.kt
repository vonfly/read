package com.vonfly.read.ui.screen.bookdetail

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.BookDetail
import com.vonfly.read.domain.usecase.AddBookToShelfUseCase
import com.vonfly.read.domain.usecase.GetBookDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class BookDetailUiState(
    val isLoading: Boolean = true,
    val bookDetail: BookDetail? = null,
    val isDescriptionExpanded: Boolean = false,
    val isInShelf: Boolean = false,
    val isAddingToShelf: Boolean = false
)

sealed class BookDetailUiEvent {
    data class ShowSnackbar(val message: String) : BookDetailUiEvent()
    data object NavigateBack : BookDetailUiEvent()
    data class NavigateToContents(val bookId: String) : BookDetailUiEvent()
}

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val addBookToShelfUseCase: AddBookToShelfUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookId: String = savedStateHandle["bookId"] ?: ""

    private val _uiState = MutableStateFlow(BookDetailUiState())
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()

    private val _event = Channel<BookDetailUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        loadBookDetail()
    }

    private fun loadBookDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getBookDetailUseCase(bookId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            bookDetail = detail,
                            isInShelf = detail.isInShelf
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _event.send(BookDetailUiEvent.ShowSnackbar(error.message ?: "加载失败"))
                }
        }
    }

    fun toggleDescriptionExpanded() {
        _uiState.update { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _event.send(BookDetailUiEvent.NavigateBack)
        }
    }

    fun onShareClick() {
        viewModelScope.launch {
            _event.send(BookDetailUiEvent.ShowSnackbar("分享功能暂未开放"))
        }
    }

    fun onAddToShelfClick() {
        if (_uiState.value.isInShelf || _uiState.value.isAddingToShelf) return

        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToShelf = true) }
            addBookToShelfUseCase(bookId)
                .onSuccess {
                    _uiState.update { it.copy(isInShelf = true, isAddingToShelf = false) }
                    _event.send(BookDetailUiEvent.ShowSnackbar("已加入书架"))
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isAddingToShelf = false) }
                    _event.send(BookDetailUiEvent.ShowSnackbar(error.message ?: "加入书架失败"))
                }
        }
    }

    fun onDownloadClick() {
        viewModelScope.launch {
            _event.send(BookDetailUiEvent.ShowSnackbar("下载功能暂未开放"))
        }
    }

    fun onReadNowClick() {
        viewModelScope.launch {
            _event.send(BookDetailUiEvent.ShowSnackbar("阅读器功能暂未开放"))
        }
    }

    fun onChapterClick(chapterId: String) {
        viewModelScope.launch {
            _event.send(BookDetailUiEvent.ShowSnackbar("章节功能暂未开放"))
        }
    }

    fun onViewAllChaptersClick() {
        viewModelScope.launch {
            _event.send(BookDetailUiEvent.NavigateToContents(bookId))
        }
    }
}
