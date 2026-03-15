package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * 自动滚动阅读组件
 *
 * 内容从下往上连续滚动，类似字幕滚动效果。
 *
 * 交互流程：
 * 1. 开启自动翻页后，隐藏所有面板，开始滚动
 * 2. 点击屏幕 → 停止滚动 + 触发 onPause 回调
 * 3. 外部控制 isPaused 决定是否继续滚动
 *
 * @param pages 所有页面内容
 * @param currentPageIndex 当前页面索引
 * @param chapterTitle 章节标题
 * @param speed 滚动速度倍数（1.0f-3.0f）
 * @param isPaused 是否暂停
 * @param currentColorScheme 当前颜色主题
 * @param onPageChange 页面变化回调（用于跨页滚动时通知外部）
 * @param onPause 暂停回调（点击屏幕时触发）
 * @param onScrollToBottom 滚动到底部回调（最后一页时触发）
 * @param modifier Modifier
 */
@Composable
fun AutoScrollReader(
    pages: ImmutableList<PageContent>,
    currentPageIndex: Int,
    chapterTitle: String,
    speed: Float,
    isPaused: Boolean,
    currentColorScheme: ReaderColorScheme,
    onPageChange: (Int) -> Unit,
    onPause: () -> Unit,
    onScrollToBottom: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = LocalReaderSettings.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 记录当前渲染的页面索引，用于跨页时保持同步
    var renderedPageIndex by remember { mutableIntStateOf(currentPageIndex) }

    // 是否已完成初始布局
    var isLayoutComplete by remember { mutableStateOf(false) }

    // 同步外部页面索引（仅在页面被外部切换时同步）
    LaunchedEffect(currentPageIndex) {
        if (currentPageIndex != renderedPageIndex) {
            renderedPageIndex = currentPageIndex
            // 页面切换时重置滚动位置到顶部
            listState.scrollToItem(0)
            isLayoutComplete = false
        }
    }

    // 监听布局完成
    LaunchedEffect(listState.layoutInfo) {
        if (listState.layoutInfo.totalItemsCount > 0 && !isLayoutComplete) {
            // 延迟一帧确保布局稳定
            delay(50)
            isLayoutComplete = true
        }
    }

    // 自动滚动逻辑
    LaunchedEffect(speed, isPaused, renderedPageIndex, isLayoutComplete) {
        if (!isPaused && pages.isNotEmpty() && isLayoutComplete) {
            // 基础滚动速度：每秒滚动的像素数
            // 速度1x = 60px/s, 1.5x = 90px/s, 2x = 120px/s, 3x = 180px/s
            val pixelsPerSecond = 60f * speed
            val frameDelayMs = 16L  // ~60fps

            // 记录累计滚动距离（用于判断是否真正开始滚动）
            var totalScrolled = 0f
            // 最小滚动距离阈值（避免初始状态误判）
            val minScrollThreshold = 50f

            while (true) {
                // 检查是否滚动到当前页底部
                val layoutInfo = listState.layoutInfo
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()

                val isAtBottom = if (lastVisibleItem != null && layoutInfo.totalItemsCount > 0) {
                    val isLastItem = lastVisibleItem.index == layoutInfo.totalItemsCount - 1
                    val itemBottom = lastVisibleItem.offset + lastVisibleItem.size
                    val viewportHeight = layoutInfo.viewportSize.height
                    // 最后一项完全可见，且已经滚动过一定距离
                    isLastItem && itemBottom <= viewportHeight && totalScrolled > minScrollThreshold
                } else {
                    false
                }

                if (isAtBottom) {
                    if (renderedPageIndex < pages.size - 1) {
                        // 切换到下一页
                        val nextPageIndex = renderedPageIndex + 1
                        renderedPageIndex = nextPageIndex
                        onPageChange(nextPageIndex)
                        // 重置滚动位置到新页面顶部
                        listState.scrollToItem(0)
                        // 重置滚动距离计数
                        totalScrolled = 0f
                        // 短暂延迟让布局完成
                        delay(100)
                    } else {
                        // 已经是最后一页，滚动到底部
                        onScrollToBottom()
                        break
                    }
                } else {
                    // 继续向上滚动 - 使用 animateScrollBy 实现平滑滚动
                    val scrollAmount = pixelsPerSecond / 60f
                    scope.launch {
                        listState.scroll {
                            scrollBy(-scrollAmount)
                        }
                    }
                    totalScrolled += scrollAmount
                    delay(frameDelayMs)
                }
            }
        }
    }

    // 点击暂停（仅在非暂停状态时响应）
    Box(
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
        if (pages.isNotEmpty() && renderedPageIndex in pages.indices) {
            val currentPage = pages[renderedPageIndex]

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
                // 章节标题（如果有）
                if (chapterTitle.isNotEmpty()) {
                    item(key = "chapter_title") {
                        Text(
                            text = chapterTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = ForegroundSecondary
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // 段落列表
                items(
                    count = currentPage.paragraphs.size,
                    key = { index -> "${currentPage.pageIndex}_$index" }
                ) { index ->
                    Text(
                        text = currentPage.paragraphs[index],
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
