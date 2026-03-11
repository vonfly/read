package com.vonfly.read.ui.screen.reader

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.vonfly.read.ui.theme.ReaderSettings
import com.vonfly.read.ui.theme.ReaderTheme
import kotlinx.coroutines.launch

/**
 * 阅读器屏幕（有状态层）
 */
@Composable
fun ReaderScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 处理事件
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ReaderUiEvent.ShowSnackbar -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                ReaderUiEvent.NavigateBack -> onNavigateBack()
            }
        }
    }

    // 屏幕常亮
    KeepScreenOn(enabled = true)

    ReaderTheme(settings = ReaderSettings()) {
        ReaderContent(
            uiState = uiState,
            onPreviousPage = viewModel::goToPreviousPage,
            onNextPage = viewModel::goToNextPage,
            onToggleControlBar = viewModel::toggleControlBar,
            onHideControlBar = viewModel::hideControlBar,
            onBackClick = viewModel::onBackClick,
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.displayCutout)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        )
    }
}

/**
 * 屏幕常亮（阅读时防锁屏）
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
