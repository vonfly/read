package com.vonfly.read.ui.screen.bookmark

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.Bookmark
import com.vonfly.read.domain.usecase.GetBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()

    private val _event = Channel<BookmarkUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        loadData(getBookmarksUseCase)
    }

    private fun loadData(getBookmarksUseCase: GetBookmarksUseCase) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getBookmarksUseCase().collect { bookmarks ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        bookmarks = bookmarks.toPersistentList()
                    )
                }
            }
        }
    }

    fun onNavigateBack() {
        viewModelScope.launch {
            _event.send(BookmarkUiEvent.NavigateBack)
        }
    }

    fun onBookmarkClick(bookmark: Bookmark) {
        viewModelScope.launch {
            // 本期显示 Toast，后续实现跳转到阅读器对应位置
            _event.send(BookmarkUiEvent.ShowSnackbar("即将跳转到《${bookmark.title}》第 ${bookmark.chapter} 章"))
        }
    }
}
