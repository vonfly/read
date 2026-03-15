package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.PageContent
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.domain.model.ReaderSettings
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.LocalReaderSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 滑动翻页组件
 *
 * 使用 HorizontalPager 实现左右滑动翻页，同时支持点击区域翻页。
 *
 * @param pages 页面内容列表
 * @param currentPageIndex 当前页面索引
 * @param chapterTitle 章节标题
 * @param onPageChange 页面变化回调
 * @param onScreenClick 中间区域点击回调（用于显示控制栏）
 * @param modifier Modifier
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageTurnSlide(
    pages: ImmutableList<PageContent>,
    currentPageIndex: Int,
    chapterTitle: String,
    onPageChange: (Int) -> Unit,
    onScreenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = LocalReaderSettings.current
    val colorScheme = settings.colorScheme
    val scope = rememberCoroutineScope()

    // 初始化 PagerState
    val pagerState = rememberPagerState(
        initialPage = currentPageIndex,
        pageCount = { pages.size }
    )

    // 监听 pagerState.currentPage 变化，通知外部
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                if (page != currentPageIndex) {
                    onPageChange(page)
                }
            }
    }

    // 同步外部 currentPageIndex 到 pagerState
    LaunchedEffect(currentPageIndex) {
        if (currentPageIndex != pagerState.currentPage) {
            pagerState.animateScrollToPage(currentPageIndex)
        }
    }

    // 外层 Box 处理点击区域逻辑
    Box(
        modifier = modifier
            .background(colorScheme.background)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    val up = waitForUpOrCancellation()

                    if (up != null) {
                        val screenWidth = size.width
                        val tapX = up.position.x
                        val leftZone = screenWidth * 0.35f
                        val rightZone = screenWidth * 0.65f

                        when {
                            tapX < leftZone -> {
                                // 左边区域：上一页
                                scope.animateToPage(pagerState, pages.size, -1)
                            }
                            tapX > rightZone -> {
                                // 右边区域：下一页
                                scope.animateToPage(pagerState, pages.size, 1)
                            }
                            else -> {
                                // 中间区域：显示控制栏
                                onScreenClick()
                            }
                        }
                    }
                }
            }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { page -> pages[page].pageIndex }
        ) { page ->
            val pageContent = pages[page]
            ReaderPageContent(
                page = pageContent,
                chapterTitle = chapterTitle,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 在协程中执行翻页动画
 */
private fun CoroutineScope.animateToPage(
    pagerState: PagerState,
    totalPages: Int,
    delta: Int
) {
    launch {
        val targetPage = (pagerState.currentPage + delta)
            .coerceIn(0, totalPages - 1)
        if (targetPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }
}

/**
 * 阅读页面内容
 */
@Composable
private fun ReaderPageContent(
    page: PageContent,
    chapterTitle: String,
    modifier: Modifier = Modifier
) {
    val settings = LocalReaderSettings.current
    val colorScheme = settings.colorScheme

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
                lineHeight = settings.fontSize * settings.lineHeight,
                letterSpacing = settings.letterSpacing.sp,
                color = colorScheme.text
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
private fun PageTurnSlidePreview() {
    AppTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalReaderSettings provides ReaderSettings()
        ) {
            PageTurnSlide(
                pages = persistentListOf(
                    PageContent(
                        pageIndex = 0,
                        chapterTitle = "第1章 科学边界",
                        paragraphs = listOf(
                            "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。这是她父亲留下的遗物，一份关于红岸基地的绝密档案。",
                            "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。叶文洁轻轻抚摸着纸张，仿佛能感受到父亲当年的气息。"
                        )
                    ),
                    PageContent(
                        pageIndex = 1,
                        chapterTitle = "第1章 科学边界",
                        paragraphs = listOf(
                            "红岸基地，一个在冷战时期建立的神秘军事设施，曾经是人类向宇宙发出第一次呼唤的地方。",
                            "那份档案记录了一段鲜为人知的历史，一段关于人类与外星文明首次接触的秘密。"
                        )
                    ),
                    PageContent(
                        pageIndex = 2,
                        chapterTitle = "第1章 科学边界",
                        paragraphs = listOf(
                            "叶文洁继续翻阅着档案，每一页都让她更加震惊。",
                            "原来，这一切的背后，隐藏着一个改变人类命运的发现。"
                        )
                    )
                ),
                currentPageIndex = 0,
                chapterTitle = "第1章 科学边界",
                onPageChange = {},
                onScreenClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PageTurnSlideDarkPreview() {
    AppTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalReaderSettings provides ReaderSettings(
                colorScheme = ReaderColorScheme.Night
            )
        ) {
            PageTurnSlide(
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
                chapterTitle = "第1章 科学边界",
                onPageChange = {},
                onScreenClick = {}
            )
        }
    }
}
