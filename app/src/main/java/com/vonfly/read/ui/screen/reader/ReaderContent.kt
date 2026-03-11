package com.vonfly.read.ui.screen.reader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.vonfly.read.ui.screen.reader.components.ReaderBottomBar
import com.vonfly.read.ui.screen.reader.components.ReaderTopBar
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.LocalReaderSettings
import com.vonfly.read.ui.theme.ReaderSettings
import com.vonfly.read.ui.theme.readerTextStyle
import com.vonfly.read.ui.theme.readerTitleStyle
import kotlinx.collections.immutable.persistentListOf

/**
 * 阅读器内容（无状态层）
 */
@Composable
fun ReaderContent(
    uiState: ReaderUiState,
    onPreviousPage: () -> Boolean,
    onNextPage: () -> Boolean,
    onToggleControlBar: () -> Unit,
    onHideControlBar: () -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val readerSettings = LocalReaderSettings.current
    val backgroundColor = readerSettings.colorScheme.background

    // Pager 状态
    val pagerState = rememberPagerState(
        initialPage = uiState.currentPageIndex,
        pageCount = { uiState.pages.size }
    )

    // 同步外部页面索引变化到 Pager
    LaunchedEffect(uiState.currentPageIndex) {
        if (pagerState.currentPage != uiState.currentPageIndex) {
            pagerState.animateScrollToPage(uiState.currentPageIndex)
        }
    }

    // 监听 Pager 滑动变化，同步回 ViewModel
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != uiState.currentPageIndex && !uiState.isLoading && uiState.pages.isNotEmpty()) {
            // 当用户滑动翻页时，通知 ViewModel
            if (pagerState.currentPage > uiState.currentPageIndex) {
                onNextPage()
            } else {
                onPreviousPage()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if (uiState.isLoading) {
            // 加载中状态
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "加载中...",
                    color = readerSettings.colorScheme.text
                )
            }
        } else if (uiState.pages.isEmpty()) {
            // 空内容状态
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无内容",
                    color = readerSettings.colorScheme.text
                )
            }
        } else {
            // 阅读内容 - 点击检测放在外层，避免被 HorizontalPager 消费
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            handleTap(
                                offset = offset,
                                screenWidth = size.width,
                                onPreviousPage = onPreviousPage,
                                onNextPage = onNextPage,
                                onToggleControlBar = onToggleControlBar
                            )
                        }
                    }
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    val page = uiState.pages[pageIndex]
                    PageContent(
                        page = page,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // 底部进度栏（始终显示）- 显示时间和页码
        if (!uiState.isLoading && uiState.pages.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 当前时间
                Text(
                    text = getCurrentTime(),
                    color = Color(0xFF999999),
                    fontSize = 12.sp
                )
                // 页码进度
                Text(
                    text = "${uiState.currentPageIndex + 1}/${uiState.pages.size}",
                    color = Color(0xFF999999),
                    fontSize = 12.sp
                )
            }
        }

        // 控制栏（顶部 + 底部）
        AnimatedVisibility(
            visible = uiState.isControlBarVisible && !uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            ReaderTopBar(
                chapterTitle = uiState.chapterTitle,
                onBackClick = onBackClick,
                onMenuClick = { /* TODO: 目录功能 */ },
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(
            visible = uiState.isControlBarVisible && !uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ReaderBottomBar(
                currentPage = uiState.currentPageIndex + 1,
                totalPages = uiState.pages.size,
                onFontClick = { /* TODO: 字体设置 */ },
                onBrightnessClick = { /* TODO: 亮度设置 */ },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * 处理点击事件
 */
private fun handleTap(
    offset: Offset,
    screenWidth: Int,
    onPreviousPage: () -> Boolean,
    onNextPage: () -> Boolean,
    onToggleControlBar: () -> Unit
) {
    val thirdWidth = screenWidth / 3
    when {
        offset.x < thirdWidth -> {
            // 左 1/3：上一页
            onPreviousPage()
        }
        offset.x > screenWidth - thirdWidth -> {
            // 右 1/3：下一页
            onNextPage()
        }
        else -> {
            // 中央 1/3：切换控制栏
            onToggleControlBar()
        }
    }
}

/**
 * 获取当前时间（格式：HH:mm）
 */
private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}

/**
 * 单页内容
 */
@Composable
private fun PageContent(
    page: com.vonfly.read.domain.model.PageContent,
    modifier: Modifier = Modifier
) {
    val readerSettings = LocalReaderSettings.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // 章节标题
        page.chapterTitle?.let { title ->
            Text(
                text = title,
                style = readerTitleStyle()
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 段落内容
        page.paragraphs.forEach { paragraph ->
            Text(
                text = paragraph,
                style = readerTextStyle()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderContentPreview() {
    AppTheme {
        val mockPages = persistentListOf(
            com.vonfly.read.domain.model.PageContent(
                pageIndex = 0,
                chapterTitle = "第1章 科学边界",
                paragraphs = listOf(
                    "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。",
                    "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。"
                )
            )
        )

        ReaderContent(
            uiState = ReaderUiState(
                isLoading = false,
                pages = mockPages,
                currentPageIndex = 0,
                chapterTitle = "第1章 科学边界"
            ),
            onPreviousPage = { false },
            onNextPage = { true },
            onToggleControlBar = {},
            onHideControlBar = {},
            onBackClick = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
