package com.vonfly.read.ui.screen.booklist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.Book
import com.vonfly.read.domain.usecase.DeleteBookUseCase
import com.vonfly.read.domain.usecase.GetBookListUseCase
import com.vonfly.read.domain.usecase.GetRecentBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class BookListUiState(
    val isLoading: Boolean = false,
    val deletingBookId: String? = null
)

sealed class BookListUiEvent {
    data class ShowSnackbar(val message: String) : BookListUiEvent()
    data class NavigateToBookDetail(val bookId: String) : BookListUiEvent()
}

@HiltViewModel
class BookListViewModel @Inject constructor(
    getBookListUseCase: GetBookListUseCase,
    getRecentBookUseCase: GetRecentBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private val _event = Channel<BookListUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    val books: StateFlow<ImmutableList<Book>> = getBookListUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = persistentListOf()
        )

    val recentBook: StateFlow<Book?> = getRecentBookUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _showDeleteDialog = MutableStateFlow<String?>(null)
    val showDeleteDialog: StateFlow<String?> = _showDeleteDialog.asStateFlow()

    fun onBookClick(id: String) {
        viewModelScope.launch {
            _event.send(BookListUiEvent.NavigateToBookDetail(id))
        }
    }

    fun onBookLongPress(id: String) {
        _showDeleteDialog.value = id
    }

    fun onDeleteConfirm(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(deletingBookId = id) }
            deleteBookUseCase(id)
                .onSuccess {
                    _showDeleteDialog.value = null
                }
                .onFailure { error ->
                    _event.send(BookListUiEvent.ShowSnackbar(error.message ?: "删除失败"))
                }
            _uiState.update { it.copy(deletingBookId = null) }
        }
    }

    fun onDeleteDismiss() {
        _showDeleteDialog.value = null
    }
}
