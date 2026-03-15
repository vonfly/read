package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.PageContent
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.LocalReaderSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * 自动滚动阅读组件（整章滚动模式）
 *
 * 将所有页面的段落合并成一个列表，从下往上连续滚动。
 *
 * 交互流程：
 * 1. 开启自动翻页后，隐藏所有面板，开始滚动
 * 2. 点击屏幕 → 停止滚动 + 触发 onPause 回调
 * 3. 外部控制 isPaused 决定是否继续滚动
 *
 * @param pages 所有页面内容（保持原有数据结构，翻页模式需要）
 * @param chapterTitle 章节标题
 * @param speed 滚动速度倍数（1.0f-3.0f）
 * @param isPaused 是否暂停
 * @param currentColorScheme 当前颜色主题
 * @param onScrollToBottom 滚动到底部回调
 * @param onPause 暂停回调（点击屏幕时触发）
 * @param modifier Modifier
 */
@Composable
fun AutoScrollReader(
    pages: ImmutableList<PageContent>,
    chapterTitle: String,
    speed: Float,
    isPaused: Boolean,
    currentColorScheme: ReaderColorScheme,
    onScrollToBottom: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier,
    // 保留参数签名兼容性，但不再使用
    currentPageIndex: Int = 0,
    onPageChange: (Int) -> Unit = {}
) {
    val settings = LocalReaderSettings.current
    val listState = rememberLazyListState()

    // 合并所有段落到一个列表
    val allParagraphs: List<ParagraphItem> = remember(pages, chapterTitle) {
        buildList {
            // 添加章节标题（如果有）
            if (chapterTitle.isNotEmpty()) {
                add(ParagraphItem.Title(chapterTitle))
            }
            // 合并所有页面的段落
            pages.forEach { page ->
                page.paragraphs.forEach { paragraph ->
                    add(ParagraphItem.Text(paragraph))
                }
            }
        }
    }

    // 布局完成状态
    var isLayoutComplete by remember { mutableStateOf(false) }
    // 累计滚动距离（用于避免误判底部）
    var totalScrolled by remember { mutableFloatStateOf(0f) }

    // 监听布局完成
    LaunchedEffect(allParagraphs) {
        // 等待 LazyColumn 完成布局
        delay(50)
        isLayoutComplete = true
    }

    // 自动滚动逻辑
    LaunchedEffect(speed, isPaused, isLayoutComplete, allParagraphs.isNotEmpty()) {
        if (!isPaused && allParagraphs.isNotEmpty() && isLayoutComplete) {
            // 基础滚动速度：每秒滚动的像素数
            // 速度1x = 60px/s, 1.5x = 90px/s, 2x = 120px/s, 3x = 180px/s
            val pixelsPerSecond = 60f * speed
            val frameDelayMs = 16L  // ~60fps
            val scrollPerFrame = pixelsPerSecond / 60f

            while (isActive) {
                // 检测是否到达底部
                val layoutInfo = listState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

                val isAtBottom = if (lastVisibleItem != null && layoutInfo.totalItemsCount > 0) {
                    val isLastItem = lastVisibleItem.index == layoutInfo.totalItemsCount - 1
                    // 使用 viewportEndOffset 而不是 viewportSize.height
                    val viewportEnd = layoutInfo.viewportEndOffset
                    val itemBottom = lastVisibleItem.offset + lastVisibleItem.size
                    // 最后一项完全可见，且已经滚动了一定距离（避免误判）
                    isLastItem && itemBottom <= viewportEnd && totalScrolled > 50f
                } else {
                    false
                }

                if (isAtBottom) {
                    onScrollToBottom()
                    break
                } else {
                    // 继续向下滚动（正值 = 内容向上移动）
                    // scrollBy 正值 = 向下滚动 = 内容向上移动
                    listState.scroll {
                        scrollBy(scrollPerFrame)
                    }
                    totalScrolled += scrollPerFrame
                    delay(frameDelayMs)
                }
            }
        }
    }

    // 重置滚动距离当暂停状态改变
    LaunchedEffect(isPaused) {
        if (!isPaused) {
            totalScrolled = 0f
        }
    }

    // 点击暂停（仅在非暂停状态时响应）
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxSize()
            .background(currentColorScheme.background)
            .then(
                if (!isPaused) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { onPause() }
                    }
                } else {
                    Modifier
                }
            )
    ) {
        if (allParagraphs.isNotEmpty()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 28.dp,
                    bottom = 72.dp  // 底部留出 Footer 空间
                )
            ) {
                items(
                    count = allParagraphs.size,
                    key = { index -> "paragraph_$index" }
                ) { index ->
                    val item = allParagraphs[index]
                    when (item) {
                        is ParagraphItem.Title -> {
                            Text(
                                text = item.text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = ForegroundSecondary
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        is ParagraphItem.Text -> {
                            Text(
                                text = item.text,
                                fontSize = settings.fontSize,
                                lineHeight = settings.fontSize * settings.lineHeight,
                                letterSpacing = settings.letterSpacing.sp,
                                color = currentColorScheme.text
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * 段落项类型
 */
private sealed class ParagraphItem {
    data class Title(val text: String) : ParagraphItem()
    data class Text(val text: String) : ParagraphItem()
}
