package com.vonfly.read.ui.screen.reader.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

/**
 * 翻页方向
 */
private enum class PageTurnDirection {
    PREVIOUS,  // 上一页
    NEXT       // 下一页
}

/**
 * 覆盖翻页组件
 *
 * 使用 AnimatedContent + slideInHorizontally / slideOutHorizontally 实现覆盖翻页效果。
 * - 翻到下一页：新页面从右侧滑入覆盖旧页面
 * - 翻到上一页：新页面从左侧滑入覆盖旧页面
 *
 * @param page 当前页面内容
 * @param chapterTitle 章节标题
 * @param onPreviousPage 翻到上一页回调
 * @param onNextPage 翻到下一页回调
 * @param onScreenClick 中间区域点击回调（用于显示控制栏）
 * @param modifier Modifier
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun PageTurnCover(
    page: PageContent,
    chapterTitle: String,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onScreenClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = LocalReaderSettings.current
    val colorScheme = settings.colorScheme

    // 记录翻页方向，用于决定动画方向
    var turnDirection by remember { mutableStateOf(PageTurnDirection.NEXT) }

    // 使用 page.pageIndex 作为 AnimatedContent 的 key
    // 当 pageIndex 变化时触发动画
    Box(
        modifier = modifier
            .fillMaxSize()
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
                                turnDirection = PageTurnDirection.PREVIOUS
                                onPreviousPage()
                            }
                            tapX > rightZone -> {
                                // 右边区域：下一页
                                turnDirection = PageTurnDirection.NEXT
                                onNextPage()
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
        AnimatedContent(
            targetState = page.pageIndex,
            transitionSpec = {
                // 根据翻页方向决定动画
                if (turnDirection == PageTurnDirection.NEXT) {
                    // 下一页：新页面从右侧滑入，旧页面向左滑出
                    slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { it }  // 从右侧滑入（完整宽度）
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetX = { -it }  // 向左滑出
                    )
                } else {
                    // 上一页：新页面从左侧滑入，旧页面向右滑出
                    slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { -it }  // 从左侧滑入
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        targetOffsetX = { it }  // 向右滑出
                    )
                }
            },
            label = "PageTurnCoverAnimation"
        ) { targetPageIndex ->
            // AnimatedContent 的 content lambda 需要使用 targetState
            // 这里我们直接使用传入的 page，因为 targetState 就是 page.pageIndex
            ReaderPageContent(
                page = page,
                chapterTitle = chapterTitle,
                modifier = Modifier.fillMaxSize()
            )
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
private fun PageTurnCoverPreview() {
    AppTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalReaderSettings provides ReaderSettings()
        ) {
            PageTurnCover(
                page = PageContent(
                    pageIndex = 0,
                    chapterTitle = "第1章 科学边界",
                    paragraphs = listOf(
                        "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。这是她父亲留下的遗物，一份关于红岸基地的绝密档案。",
                        "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。叶文洁轻轻抚摸着纸张，仿佛能感受到父亲当年的气息。"
                    )
                ),
                chapterTitle = "第1章 科学边界",
                onPreviousPage = {},
                onNextPage = {},
                onScreenClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PageTurnCoverDarkPreview() {
    AppTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalReaderSettings provides ReaderSettings(
                colorScheme = ReaderColorScheme.Night
            )
        ) {
            PageTurnCover(
                page = PageContent(
                    pageIndex = 0,
                    chapterTitle = "第1章 科学边界",
                    paragraphs = listOf(
                        "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。",
                        "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。"
                    )
                ),
                chapterTitle = "第1章 科学边界",
                onPreviousPage = {},
                onNextPage = {},
                onScreenClick = {}
            )
        }
    }
}
