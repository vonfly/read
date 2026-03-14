package com.vonfly.read.ui.screen.bookdetail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun BookDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToContents: (String) -> Unit,
    onNavigateToReader: (String) -> Unit,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is BookDetailUiEvent.ShowSnackbar -> {
                    // 使用 launch 异步显示 Snackbar，避免阻塞事件流
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                BookDetailUiEvent.NavigateBack -> onNavigateBack()
                is BookDetailUiEvent.NavigateToContents -> onNavigateToContents(event.bookId)
            }
        }
    }

    BookDetailContent(
        uiState = uiState,
        onBackClick = viewModel::onBackClick,
        onShareClick = viewModel::onShareClick,
        onToggleDescriptionExpanded = viewModel::toggleDescriptionExpanded,
        onAddToShelfClick = viewModel::onAddToShelfClick,
        onDownloadClick = viewModel::onDownloadClick,
        onReadNowClick = { onNavigateToReader(viewModel.bookId) },
        onChapterClick = viewModel::onChapterClick,
        onViewAllChaptersClick = viewModel::onViewAllChaptersClick,
        modifier = Modifier.fillMaxSize()
    )
}
