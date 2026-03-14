package com.vonfly.read.ui.screen.reader

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.PageContent
import com.vonfly.read.domain.model.ReaderSettings
import com.vonfly.read.ui.screen.reader.components.ReaderBottomBar
import com.vonfly.read.ui.screen.reader.components.ReaderFooter
import com.vonfly.read.ui.screen.reader.components.ReaderTopBar
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.LocalReaderSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ReaderContent(
    uiState: ReaderUiState,
    onScreenClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onProgressChange: (Float) -> Unit,
    onAddToShelfClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onFontClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onMoreClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    isInShelf: Boolean = false,
    isBookmarked: Boolean = false
) {
    val settings = LocalReaderSettings.current
    val colorScheme = settings.colorScheme

    Box(
        modifier = modifier
            .background(colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    // 点击屏幕中央区域切换控制栏
                    val screenWidth = size.width
                    val screenHeight = size.height
                    val centerX = screenWidth / 2
                    val centerY = screenHeight / 2
                    val tapZoneWidth = screenWidth * 0.3f
                    val tapZoneHeight = screenHeight * 0.3f

                    val isInCenter = offset.x in (centerX - tapZoneWidth)..(centerX + tapZoneWidth) &&
                            offset.y in (centerY - tapZoneHeight)..(centerY + tapZoneHeight)

                    if (isInCenter) {
                        onScreenClick()
                    }
                }
            }
    ) {
        if (uiState.isLoading) {
            // 加载状态
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.pages.isNotEmpty()) {
            // 阅读内容
            ReaderPageLayer(
                page = uiState.pages[uiState.currentPageIndex],
                chapterTitle = uiState.chapterTitle,
                modifier = Modifier.fillMaxSize()
            )

            // 底部状态栏（时间 + 页码）
            ReaderFooter(
                currentPage = uiState.currentPageIndex + 1,
                totalPages = uiState.totalPages,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
        }

        // 控制栏（顶部 + 底部）
        AnimatedVisibility(
            visible = uiState.isControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // 顶部控制栏
                ReaderTopBar(
                    bookTitle = uiState.bookTitle.ifEmpty { "三体" },
                    onNavigateBack = onNavigateBack,
                    onAddToShelfClick = onAddToShelfClick,
                    onBookmarkClick = onBookmarkClick,
                    onSettingsClick = onSettingsClick,
                    isInShelf = isInShelf,
                    isBookmarked = isBookmarked,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                // 底部控制栏
                ReaderBottomBar(
                    currentPage = uiState.currentPageIndex + 1,
                    totalPages = uiState.totalPages,
                    onPreviousPage = onPreviousPage,
                    onNextPage = onNextPage,
                    onProgressChange = onProgressChange,
                    onCatalogClick = onCatalogClick,
                    onFontClick = onFontClick,
                    onBrightnessClick = onBrightnessClick,
                    onMoreClick = onMoreClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * 阅读页面内容层
 */
@Composable
private fun ReaderPageLayer(
    page: PageContent,
    chapterTitle: String,
    modifier: Modifier = Modifier
) {
    val settings = LocalReaderSettings.current
    val colorScheme = settings.colorScheme
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 章节标题（如果有）
        if (chapterTitle.isNotEmpty()) {
            item(key = "chapter_title") {
                Column {
                    Spacer(modifier = Modifier.height(28.dp))
                    Text(
                        text = chapterTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ForegroundSecondary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            // 没有章节标题时，顶部留白
            item(key = "top_spacer") {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }

        // 段落列表
        items(
            items = page.paragraphs,
            key = { paragraph -> "${page.pageIndex}_${paragraph.hashCode()}" }
        ) { paragraph ->
            Text(
                text = paragraph,
                fontSize = settings.fontSize,
                lineHeight = settings.fontSize * 1.3f, // 设计稿 1.8 对应 Compose 1.3
                color = colorScheme.text,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            // 长按复制
                            clipboardManager.setText(AnnotatedString(paragraph))
                            Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
        }

        // 底部留白（为 Footer 留空间）
        item(key = "bottom_spacer") {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderContentPreview() {
    AppTheme {
        CompositionLocalProvider(
            LocalReaderSettings provides ReaderSettings()
        ) {
            ReaderContent(
                uiState = ReaderUiState(
                    isLoading = false,
                    bookTitle = "三体",
                    chapterTitle = "第1章 科学边界",
                    pages = persistentListOf(
                        PageContent(
                            pageIndex = 0,
                            chapterTitle = "第1章 科学边界",
                            paragraphs = listOf(
                                "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。这是她父亲留下的遗物，一份关于红岸基地的绝密档案。",
                                "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。叶文洁轻轻抚摸着纸张，仿佛能感受到父亲当年的气息。"
                            )
                        )
                    ),
                    currentPageIndex = 0,
                    totalPages = 7,
                    isControlsVisible = false
                ),
                onScreenClick = {},
                onNavigateBack = {},
                onPreviousPage = {},
                onNextPage = {},
                onProgressChange = {},
                onAddToShelfClick = {},
                onBookmarkClick = {},
                onSettingsClick = {},
                onCatalogClick = {},
                onFontClick = {},
                onBrightnessClick = {},
                onMoreClick = {},
                snackbarHostState = remember { SnackbarHostState() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderContentWithControlsPreview() {
    AppTheme {
        CompositionLocalProvider(
            LocalReaderSettings provides ReaderSettings()
        ) {
            ReaderContent(
                uiState = ReaderUiState(
                    isLoading = false,
                    bookTitle = "三体",
                    chapterTitle = "第1章 科学边界",
                    pages = persistentListOf(
                        PageContent(
                            pageIndex = 0,
                            chapterTitle = "第1章 科学边界",
                            paragraphs = listOf(
                                "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。",
                                "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。"
                            )
                        )
                    ),
                    currentPageIndex = 0,
                    totalPages = 7,
                    isControlsVisible = true
                ),
                onScreenClick = {},
                onNavigateBack = {},
                onPreviousPage = {},
                onNextPage = {},
                onProgressChange = {},
                onAddToShelfClick = {},
                onBookmarkClick = {},
                onSettingsClick = {},
                onCatalogClick = {},
                onFontClick = {},
                onBrightnessClick = {},
                onMoreClick = {},
                snackbarHostState = remember { SnackbarHostState() }
            )
        }
    }
}
