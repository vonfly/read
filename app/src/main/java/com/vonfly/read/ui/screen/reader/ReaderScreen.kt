package com.vonfly.read.ui.screen.reader

import android.app.Activity
import android.util.Log
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

    // 应用屏幕亮度
    ScreenBrightness(uiState.readerSettings.brightness)

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
            onFontSizeChange = viewModel::onFontSizeChange,
            onLineHeightChange = viewModel::onLineHeightChange,
            onLetterSpacingChange = viewModel::onLetterSpacingChange,
            onPageTurnModeChange = viewModel::onPageTurnModeChange,
            onAutoPageEnabledChange = viewModel::onAutoPageEnabledChange,
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

/**
 * 屏幕亮度控制
 *
 * 通过修改 Window.attributes.screenBrightness 控制当前 Activity 的屏幕亮度。
 * 注意：此设置只影响当前应用窗口，不会修改系统全局亮度。
 *
 * @param brightness 亮度值 0.0-1.0
 */
@Composable
private fun ScreenBrightness(brightness: Float) {
    val context = LocalContext.current
    val activity = context as? Activity

    // 调试日志
    LaunchedEffect(brightness) {
        Log.d("ReaderScreen", "ScreenBrightness: brightness=$brightness")
    }

    DisposableEffect(brightness) {
        Log.d("ReaderScreen", "DisposableEffect: setting brightness=$brightness")
        activity?.window?.let { window ->
            val layoutParams = window.attributes
            // 0.0 表示使用系统亮度，但为了用户体验，最小值设为 0.01
            // 避免 0.0 时用户误认为功能失效
            layoutParams.screenBrightness = brightness.coerceIn(0.01f, 1.0f)
            window.attributes = layoutParams
            Log.d("ReaderScreen", "Screen brightness set to: ${layoutParams.screenBrightness}")
        }
        onDispose {
            // 离开阅读器时恢复系统默认亮度
            Log.d("ReaderScreen", "onDispose: restoring system brightness")
            activity?.window?.let { window ->
                val layoutParams = window.attributes
                layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                window.attributes = layoutParams
            }
        }
    }
}
