package com.vonfly.read.ui.screen.bookmark

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.vonfly.read.ui.theme.Background
import kotlinx.coroutines.launch

@Composable
fun BookmarkScreen(
    onNavigateBack: () -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is BookmarkUiEvent.ShowSnackbar -> {
                    // 使用 launch 异步显示，避免阻塞事件流
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                BookmarkUiEvent.NavigateBack -> onNavigateBack()
                is BookmarkUiEvent.NavigateToReader -> {
                    // 后续实现：跳转到阅读器对应位置
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { innerPadding ->
        BookmarkContent(
            uiState = uiState,
            onNavigateBack = viewModel::onNavigateBack,
            onBookmarkClick = viewModel::onBookmarkClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
