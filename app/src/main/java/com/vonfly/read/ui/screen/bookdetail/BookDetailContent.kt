package com.vonfly.read.ui.screen.bookdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.BookDetail
import com.vonfly.read.domain.model.Chapter
import com.vonfly.read.ui.screen.bookdetail.components.BottomActionBar
import com.vonfly.read.ui.screen.bookdetail.components.BookInfoSection
import com.vonfly.read.ui.screen.bookdetail.components.ChapterSection
import com.vonfly.read.ui.screen.bookdetail.components.DescriptionSection
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Background
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.Spacing
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BookDetailContent(
    uiState: BookDetailUiState,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onToggleDescriptionExpanded: () -> Unit,
    onAddToShelfClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onReadNowClick: () -> Unit,
    onChapterClick: (String) -> Unit,
    onViewAllChaptersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            BookDetailHeader(
                onBackClick = onBackClick,
                onShareClick = onShareClick
            )
        },
        bottomBar = {
            if (uiState.bookDetail != null) {
                BottomActionBar(
                    isInShelf = uiState.isInShelf,
                    isAddingToShelf = uiState.isAddingToShelf,
                    onAddToShelfClick = onAddToShelfClick,
                    onDownloadClick = onDownloadClick,
                    onReadNowClick = onReadNowClick
                )
            }
        },
        containerColor = Background,
        modifier = modifier
    ) { innerPadding ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.bookDetail != null) {
            val bookDetail = uiState.bookDetail
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.Lg)
            ) {
                // Book Info Section
                BookInfoSection(
                    title = bookDetail.title,
                    author = bookDetail.author,
                    coverGradientStart = bookDetail.coverGradientStart,
                    coverGradientEnd = bookDetail.coverGradientEnd,
                    rating = bookDetail.rating,
                    readerCount = bookDetail.readerCount
                )

                Spacer(modifier = Modifier.height(Spacing.Xl))

                // Description Section
                DescriptionSection(
                    description = bookDetail.description,
                    isExpanded = uiState.isDescriptionExpanded,
                    onToggleExpand = onToggleDescriptionExpanded
                )

                Spacer(modifier = Modifier.height(Spacing.Xl))

                // Chapter Section
                ChapterSection(
                    totalChapters = bookDetail.totalChapters,
                    chapters = bookDetail.chapters,
                    onChapterClick = onChapterClick,
                    onViewAllClick = onViewAllChaptersClick
                )

                // Extra space for bottom bar
                Spacer(modifier = Modifier.height(Spacing.Xl))
            }
        }
    }
}

@Composable
private fun BookDetailHeader(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Card)
            .padding(horizontal = Spacing.Sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "返回",
                modifier = Modifier.size(24.dp),
                tint = Foreground
            )
        }

        IconButton(onClick = onShareClick) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "分享",
                modifier = Modifier.size(24.dp),
                tint = Foreground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDetailContentPreview() {
    AppTheme {
        BookDetailContent(
            uiState = BookDetailUiState(
                isLoading = false,
                bookDetail = BookDetail(
                    id = "1",
                    title = "三体",
                    author = "刘慈欣",
                    coverGradientStart = "#667eea",
                    coverGradientEnd = "#764ba2",
                    rating = 9.4f,
                    readerCount = 125680,
                    description = "这是一部关于人类文明与外星文明博弈的科幻史诗。文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划「红岸工程」取得了突破性进展。",
                    totalChapters = 256,
                    isInShelf = false,
                    chapters = persistentListOf(
                        Chapter(
                            id = "1",
                            bookId = "1",
                            title = "第1章 科学边界",
                            index = 0,
                            isFree = true
                        ),
                        Chapter(
                            id = "2",
                            bookId = "1",
                            title = "第2章 三体",
                            index = 1,
                            isFree = false
                        )
                    )
                ),
                isDescriptionExpanded = false,
                isInShelf = false
            ),
            onBackClick = {},
            onShareClick = {},
            onToggleDescriptionExpanded = {},
            onAddToShelfClick = {},
            onDownloadClick = {},
            onReadNowClick = {},
            onChapterClick = {},
            onViewAllChaptersClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookDetailContentLoadingPreview() {
    AppTheme {
        BookDetailContent(
            uiState = BookDetailUiState(
                isLoading = true
            ),
            onBackClick = {},
            onShareClick = {},
            onToggleDescriptionExpanded = {},
            onAddToShelfClick = {},
            onDownloadClick = {},
            onReadNowClick = {},
            onChapterClick = {},
            onViewAllChaptersClick = {}
        )
    }
}
