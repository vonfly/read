package com.vonfly.read.ui.screen.reader

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vonfly.read.ui.theme.ReaderTheme
import kotlinx.coroutines.launch

@Composable
fun ReaderScreen(
    bookId: String,
    onNavigateBack: () -> Unit,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 屏幕常亮
    KeepScreenOn(enabled = true)

    // 事件处理
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ReaderUiEvent.ShowSnackbar -> {
                    // 使用 launch 异步显示 Snackbar，避免阻塞事件流
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                ReaderUiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    ReaderTheme(settings = uiState.readerSettings) {
        ReaderContent(
            uiState = uiState,
            onScreenClick = viewModel::toggleControlsVisibility,
            onNavigateBack = viewModel::onNavigateBack,
            onPreviousPage = viewModel::onPreviousPage,
            onNextPage = viewModel::onNextPage,
            onProgressChange = viewModel::onProgressChange,
            onAddToShelfClick = viewModel::onAddToShelfClick,
            onBookmarkClick = viewModel::onBookmarkClick,
            onSettingsClick = viewModel::onSettingsClick,
            onCatalogClick = viewModel::onCatalogClick,
            onFontClick = viewModel::onFontClick,
            onBrightnessClick = viewModel::onBrightnessClick,
            onMoreClick = viewModel::onMoreClick,
            onChapterClick = viewModel::onChapterClick,
            onToggleSortOrder = viewModel::toggleSortOrder,
            hidePanel = viewModel::hidePanel,
            onBrightnessChange = viewModel::onBrightnessChange,
            onColorSchemeChange = viewModel::onColorSchemeChange,
            isInShelf = uiState.isInShelf,
            isBookmarked = uiState.isBookmarked,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * 屏幕常亮
 */
@Composable
private fun KeepScreenOn(enabled: Boolean = true) {
    val context = LocalContext.current
    DisposableEffect(enabled) {
        val window = (context as? Activity)?.window
        if (enabled) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
