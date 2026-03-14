package com.vonfly.read.ui.screen.profile

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
fun ProfileScreen(
    onNavigateToBookList: () -> Unit,
    onNavigateToStore: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ProfileUiEvent.ShowSnackbar -> {
                    // 使用 launch 异步显示，避免阻塞事件流
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                is ProfileUiEvent.NavigateToBookmarks -> {
                    // 后续实现
                }
                is ProfileUiEvent.NavigateToHistory -> {
                    // 后续实现
                }
                is ProfileUiEvent.NavigateToDownloads -> {
                    // 后续实现
                }
                is ProfileUiEvent.NavigateToSettings -> {
                    // 后续实现
                }
                is ProfileUiEvent.NavigateToProfileEdit -> {
                    // 后续实现
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { innerPadding ->
        ProfileContent(
            uiState = uiState,
            onProfileCardClick = viewModel::onProfileCardClick,
            onSettingsClick = viewModel::onSettingsClick,
            onBookmarksClick = viewModel::onBookmarksClick,
            onHistoryClick = viewModel::onHistoryClick,
            onDownloadsClick = viewModel::onDownloadsClick,
            onDarkModeClick = viewModel::onDarkModeClick,
            onTabClick = { tabIndex ->
                when (tabIndex) {
                    0 -> onNavigateToBookList()
                    1 -> onNavigateToStore()
                    // 2 是当前页面，不需要处理
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
