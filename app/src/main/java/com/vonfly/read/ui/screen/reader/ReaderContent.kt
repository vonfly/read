package com.vonfly.read.ui.screen.reader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.PageContent
import com.vonfly.read.domain.model.ReaderSettings
import com.vonfly.read.domain.model.PageTurnMode
import com.vonfly.read.ui.screen.reader.components.AutoPageConfigPanel
import com.vonfly.read.ui.screen.reader.components.AutoScrollReader
import com.vonfly.read.ui.screen.reader.components.BrightnessBottomPanel
import com.vonfly.read.ui.screen.reader.components.CatalogBottomPanel
import com.vonfly.read.ui.screen.reader.components.FontBottomPanel
import com.vonfly.read.ui.screen.reader.components.MoreBottomPanel
import com.vonfly.read.ui.screen.reader.components.PageTurnCover
import com.vonfly.read.ui.screen.reader.components.PageTurnSlide
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
    onPageChange: (Int) -> Unit,
    onProgressChange: (Float) -> Unit,
    onAddToShelfClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onFontClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onMoreClick: () -> Unit,
    onChapterClick: (Int) -> Unit,
    onToggleSortOrder: () -> Unit,
    hidePanel: () -> Unit,
    onBrightnessChange: (Float) -> Unit,
    onColorSchemeChange: (com.vonfly.read.domain.model.ReaderColorScheme) -> Unit,
    onFontSizeChange: (Float) -> Unit,
    onLineHeightChange: (Float) -> Unit,
    onLetterSpacingChange: (Float) -> Unit,
    onPageTurnModeChange: (com.vonfly.read.domain.model.PageTurnMode) -> Unit,
    onAutoPageEnabledChange: (Boolean) -> Unit,
    onAutoPageSpeedChange: (Float) -> Unit,
    onAutoPageIntervalChange: (Int) -> Unit,
    setAutoPagePaused: (Boolean) -> Unit,
    onScrollToBottom: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    isInShelf: Boolean = false,
    isBookmarked: Boolean = false
) {
    val settings = LocalReaderSettings.current
    val colorScheme = settings.colorScheme

    Box(
        modifier = modifier.background(colorScheme.background)
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
            // 自动滚动模式
            if (uiState.readerSettings.autoPageEnabled) {
                // 自动滚动模式
                Box(modifier = Modifier.fillMaxSize()) {
                    AutoScrollReader(
                        pages = uiState.pages,
                        currentPageIndex = uiState.currentPageIndex,
                        chapterTitle = uiState.chapterTitle,
                        speed = uiState.readerSettings.autoPageSpeed,
                        isPaused = uiState.autoPagePaused,
                        currentColorScheme = uiState.readerSettings.colorScheme,
                        onPageChange = onPageChange,
                        onPause = { setAutoPagePaused(true) },
                        onScrollToBottom = onScrollToBottom,
                        modifier = Modifier.fillMaxSize()
                    )

                    // 暂停时显示配置面板 + 透明遮罩层（点击恢复滚动）
                    if (uiState.autoPagePaused) {
                        // 透明遮罩层 - 点击恢复滚动
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    awaitEachGesture {
                                        val down = awaitFirstDown()
                                        down.consume()
                                        val up = waitForUpOrCancellation()
                                        up?.consume()
                                        // 点击遮罩层恢复滚动
                                        setAutoPagePaused(false)
                                    }
                                }
                        )

                        // 配置面板（面板自身不消费点击，让遮罩层处理）
                        AutoPageConfigPanel(
                            autoPageEnabled = true,
                            autoPageSpeed = uiState.readerSettings.autoPageSpeed,
                            autoPageInterval = uiState.readerSettings.autoPageInterval,
                            currentColorScheme = uiState.readerSettings.colorScheme,
                            onAutoPageEnabledChange = onAutoPageEnabledChange,
                            onAutoPageSpeedChange = onAutoPageSpeedChange,
                            onAutoPageIntervalChange = onAutoPageIntervalChange,
                            onCatalogClick = onCatalogClick,
                            onBrightnessClick = onBrightnessClick,
                            onFontClick = onFontClick,
                            onMoreClick = { setAutoPagePaused(false) },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            } else {
                // 正常翻页模式
                // 根据控制栏可见性和翻页模式渲染内容
                if (uiState.isControlsVisible) {
                // 面板已弹出，点击内容区域隐藏面板
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown()
                                val up = waitForUpOrCancellation()
                                if (up != null) {
                                    onScreenClick()
                                }
                            }
                        }
                ) {
                    ReaderPageLayer(
                        page = uiState.pages[uiState.currentPageIndex],
                        chapterTitle = uiState.chapterTitle,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // 面板未弹出，根据翻页模式渲染对应组件
                when (uiState.readerSettings.pageTurnMode) {
                    PageTurnMode.SLIDE -> PageTurnSlide(
                        pages = uiState.pages,
                        currentPageIndex = uiState.currentPageIndex,
                        chapterTitle = uiState.chapterTitle,
                        onPageChange = onPageChange,
                        onScreenClick = onScreenClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    PageTurnMode.COVER -> PageTurnCover(
                        page = uiState.pages[uiState.currentPageIndex],
                        chapterTitle = uiState.chapterTitle,
                        onPreviousPage = onPreviousPage,
                        onNextPage = onNextPage,
                        onScreenClick = onScreenClick,
                        modifier = Modifier.fillMaxSize()
                    )
                    PageTurnMode.REAL -> {
                        // 仿真翻页暂不实现，使用简单切换
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
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
                                                tapX < leftZone -> onPreviousPage()
                                                tapX > rightZone -> onNextPage()
                                                else -> onScreenClick()
                                            }
                                        }
                                    }
                                }
                        ) {
                            ReaderPageLayer(
                                page = uiState.pages[uiState.currentPageIndex],
                                chapterTitle = uiState.chapterTitle,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            // 底部状态栏（时间 + 页码）- 仅正常翻页模式显示
            ReaderFooter(
                currentPage = uiState.currentPageIndex + 1,
                totalPages = uiState.totalPages,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
            }
        }

        // 顶部控制栏 - 绑定 visiblePanel 状态（只有默认状态显示）
        // 拦截顶部面板区域的点击事件，阻止传播到外层
        AnimatedVisibility(
            visible = uiState.isControlsVisible && uiState.visiblePanel == null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .pointerInput(Unit) {
                    // 拦截点击事件但不做处理，阻止传播到外层
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        val up = waitForUpOrCancellation()
                        up?.consume()
                    }
                }
        ) {
            ReaderTopBar(
                bookTitle = uiState.bookTitle.ifEmpty { "三体" },
                currentColorScheme = uiState.readerSettings.colorScheme,
                onNavigateBack = onNavigateBack,
                onAddToShelfClick = onAddToShelfClick,
                onBookmarkClick = onBookmarkClick,
                onSettingsClick = onSettingsClick,
                isInShelf = isInShelf,
                isBookmarked = isBookmarked
            )
        }

        // 底部面板 - 原地切换内容，无动画
        // 拦截面板区域的点击事件，阻止传播到外层，避免点击空白区域隐藏面板
        if (uiState.isControlsVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .pointerInput(Unit) {
                        // 拦截点击事件但不做处理，阻止传播到外层
                        // 按钮点击会先被按钮自己消费，不受影响
                        awaitEachGesture {
                            val down = awaitFirstDown()
                            // 显式消费 down 事件，阻止传播到外层
                            down.consume()
                            val up = waitForUpOrCancellation()
                            // 消费 up 事件（如果存在）
                            up?.consume()
                        }
                    }
            ) {
                when (uiState.visiblePanel) {
                    null -> {
                        // 默认控制栏：进度条 + 控制按钮
                        ReaderBottomBar(
                            currentPage = uiState.currentPageIndex + 1,
                            totalPages = uiState.totalPages,
                            currentColorScheme = uiState.readerSettings.colorScheme,
                            onPreviousPage = onPreviousPage,
                            onNextPage = onNextPage,
                            onProgressChange = onProgressChange,
                            onCatalogClick = onCatalogClick,
                            onFontClick = onFontClick,
                            onBrightnessClick = onBrightnessClick,
                            onMoreClick = onMoreClick,
                            activeButton = null
                        )
                    }
                    ReaderPanel.CATALOG -> {
                        // 目录模式：章节列表 + 控制按钮
                        CatalogBottomPanel(
                            chapters = uiState.chapters,
                            currentChapterIndex = uiState.currentChapterIndex,
                            isSortAscending = uiState.isSortAscending,
                            currentColorScheme = uiState.readerSettings.colorScheme,
                            onChapterClick = onChapterClick,
                            onToggleSortOrder = onToggleSortOrder,
                            onCatalogClick = onCatalogClick,
                            onBrightnessClick = onBrightnessClick,
                            onFontClick = onFontClick,
                            onMoreClick = onMoreClick
                        )
                    }
                    ReaderPanel.BRIGHTNESS -> {
                        // 亮度面板
                        BrightnessBottomPanel(
                            brightness = uiState.readerSettings.brightness,
                            currentColorScheme = uiState.readerSettings.colorScheme,
                            onBrightnessChange = onBrightnessChange,
                            onColorSchemeChange = onColorSchemeChange,
                            onCatalogClick = onCatalogClick,
                            onBrightnessClick = onBrightnessClick,
                            onFontClick = onFontClick,
                            onMoreClick = onMoreClick
                        )
                    }
                    ReaderPanel.FONT -> {
                        FontBottomPanel(
                            fontSize = settings.fontSize.value,
                            lineHeight = settings.lineHeight,
                            letterSpacing = settings.letterSpacing,
                            currentColorScheme = colorScheme,
                            onFontSizeChange = onFontSizeChange,
                            onLineHeightChange = onLineHeightChange,
                            onLetterSpacingChange = onLetterSpacingChange,
                            onCatalogClick = onCatalogClick,
                            onBrightnessClick = onBrightnessClick,
                            onFontClick = onFontClick,
                            onMoreClick = onMoreClick
                        )
                    }
                    ReaderPanel.MORE -> MoreBottomPanel(
                        pageTurnMode = uiState.readerSettings.pageTurnMode,
                        autoPageEnabled = uiState.readerSettings.autoPageEnabled,
                        currentColorScheme = uiState.readerSettings.colorScheme,
                        onPageTurnModeChange = onPageTurnModeChange,
                        onAutoPageEnabledChange = onAutoPageEnabledChange,
                        onCatalogClick = onCatalogClick,
                        onFontClick = onFontClick,
                        onBrightnessClick = onBrightnessClick,
                        onMoreClick = onMoreClick
                    )
                }
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
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ReaderPageLayer(
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
                // 不添加任何点击/长按 modifier，让所有点击事件传播到外层 pointerInput
                // TODO: 后续如需长按复制功能，在 LazyColumn 层级使用更底层的手势检测
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
                onPageChange = {},
                onProgressChange = {},
                onAddToShelfClick = {},
                onBookmarkClick = {},
                onSettingsClick = {},
                onCatalogClick = {},
                onFontClick = {},
                onBrightnessClick = {},
                onMoreClick = {},
                onChapterClick = {},
                onToggleSortOrder = {},
                hidePanel = {},
                onBrightnessChange = {},
                onColorSchemeChange = {},
                onFontSizeChange = {},
                onLineHeightChange = {},
                onLetterSpacingChange = {},
                onPageTurnModeChange = {},
                onAutoPageEnabledChange = {},
                onAutoPageSpeedChange = {},
                onAutoPageIntervalChange = {},
                setAutoPagePaused = {},
                onScrollToBottom = {},
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
                onPageChange = {},
                onProgressChange = {},
                onAddToShelfClick = {},
                onBookmarkClick = {},
                onSettingsClick = {},
                onCatalogClick = {},
                onFontClick = {},
                onBrightnessClick = {},
                onMoreClick = {},
                onChapterClick = {},
                onToggleSortOrder = {},
                hidePanel = {},
                onBrightnessChange = {},
                onColorSchemeChange = {},
                onFontSizeChange = {},
                onLineHeightChange = {},
                onLetterSpacingChange = {},
                onPageTurnModeChange = {},
                onAutoPageEnabledChange = {},
                onAutoPageSpeedChange = {},
                onAutoPageIntervalChange = {},
                setAutoPagePaused = {},
                onScrollToBottom = {},
                snackbarHostState = remember { SnackbarHostState() }
            )
        }
    }
}
