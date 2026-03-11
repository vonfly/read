package com.vonfly.read.ui.screen.booklist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vonfly.read.ui.theme.Background

@Composable
fun BookListScreen(
    onNavigateToBookDetail: (String) -> Unit,
    onNavigateToStore: () -> Unit,
    viewModel: BookListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val books by viewModel.books.collectAsStateWithLifecycle()
    val recentBook by viewModel.recentBook.collectAsStateWithLifecycle()
    val deleteDialogBookId by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is BookListUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is BookListUiEvent.NavigateToBookDetail -> onNavigateToBookDetail(event.bookId)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { innerPadding ->
        BookListContent(
            books = books,
            recentBook = recentBook,
            isLoading = uiState.isLoading,
            deleteDialogBookId = deleteDialogBookId,
            onBookClick = viewModel::onBookClick,
            onBookLongPress = viewModel::onBookLongPress,
            onDeleteConfirm = viewModel::onDeleteConfirm,
            onDeleteDismiss = viewModel::onDeleteDismiss,
            onTabClick = { tabIndex ->
                when (tabIndex) {
                    1 -> onNavigateToStore()
                    // 2 -> onNavigateToProfile() // TODO: 我的页面
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
