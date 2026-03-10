package com.vonfly.read.ui.screen.booklist

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
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
import com.vonfly.read.domain.model.Book
import com.vonfly.read.ui.component.TabBar
import com.vonfly.read.ui.screen.booklist.components.BookGrid
import com.vonfly.read.ui.screen.booklist.components.ContinueCard
import com.vonfly.read.ui.screen.booklist.components.DeleteDialog
import com.vonfly.read.ui.screen.booklist.components.EmptyState
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Background
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.Spacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BookListContent(
    books: ImmutableList<Book>,
    recentBook: Book?,
    isLoading: Boolean,
    deleteDialogBookId: String?,
    onBookClick: (String) -> Unit,
    onBookLongPress: (String) -> Unit,
    onDeleteConfirm: (String) -> Unit,
    onDeleteDismiss: () -> Unit,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            TabBar(
                selectedIndex = 0,
                onTabClick = onTabClick
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header
            BookListHeader(
                onSearchClick = { /* 本期禁用 */ },
                onImportClick = { /* 本期禁用 */ }
            )

            // Content Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.Lg)
            ) {
                Spacer(modifier = Modifier.height(Spacing.Lg))

                // Continue Reading Section
                if (recentBook != null) {
                    ContinueReadingSection(
                        book = recentBook,
                        onBookClick = onBookClick
                    )
                    Spacer(modifier = Modifier.height(Spacing.Xl))
                }

                // Bookshelf Section
                BookshelfSection(
                    books = books,
                    onBookClick = onBookClick,
                    onBookLongPress = onBookLongPress
                )
            }
        }

        // Delete Dialog
        if (deleteDialogBookId != null) {
            DeleteDialog(
                onConfirm = { onDeleteConfirm(deleteDialogBookId) },
                onDismiss = onDeleteDismiss
            )
        }
    }
}

@Composable
private fun BookListHeader(
    onSearchClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = Spacing.Lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "书架",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Foreground
        )

        Row(horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(22.dp),
                    tint = Foreground
                )
            }
            Spacer(modifier = Modifier.width(Spacing.Xs))
            IconButton(onClick = onImportClick) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "添加",
                    modifier = Modifier.size(22.dp),
                    tint = Foreground
                )
            }
        }
    }
}

@Composable
private fun ContinueReadingSection(
    book: Book,
    onBookClick: (String) -> Unit
) {
    Column {
        com.vonfly.read.ui.component.SectionHeader(
            title = "继续阅读",
            showMore = true,
            onMoreClick = { /* 本期禁用 */ }
        )
        Spacer(modifier = Modifier.height(Spacing.Md))
        ContinueCard(
            book = book,
            onClick = { onBookClick(book.id) }
        )
    }
}

@Composable
private fun BookshelfSection(
    books: ImmutableList<Book>,
    onBookClick: (String) -> Unit,
    onBookLongPress: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        com.vonfly.read.ui.component.SectionHeader(
            title = "我的书架",
            showMore = false
        )
        Spacer(modifier = Modifier.height(Spacing.Md))

        if (books.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            BookGrid(
                books = books,
                onBookClick = onBookClick,
                onBookLongPress = onBookLongPress,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookListContentPreview() {
    AppTheme {
        BookListContent(
            books = persistentListOf(
                Book(
                    id = "1",
                    title = "三体",
                    coverGradientStart = "#667eea",
                    coverGradientEnd = "#764ba2",
                    lastReadAt = System.currentTimeMillis(),
                    readingProgress = 0.35f,
                    currentChapter = "第一章",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            ),
            recentBook = Book(
                id = "1",
                title = "三体",
                coverGradientStart = "#667eea",
                coverGradientEnd = "#764ba2",
                lastReadAt = System.currentTimeMillis(),
                readingProgress = 0.35f,
                currentChapter = "第一章",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            isLoading = false,
            deleteDialogBookId = null,
            onBookClick = {},
            onBookLongPress = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {},
            onTabClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookListContentEmptyPreview() {
    AppTheme {
        BookListContent(
            books = persistentListOf(),
            recentBook = null,
            isLoading = false,
            deleteDialogBookId = null,
            onBookClick = {},
            onBookLongPress = {},
            onDeleteConfirm = {},
            onDeleteDismiss = {},
            onTabClick = {}
        )
    }
}
