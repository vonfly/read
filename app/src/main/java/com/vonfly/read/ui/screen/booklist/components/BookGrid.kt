package com.vonfly.read.ui.screen.booklist.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.Book
import com.vonfly.read.ui.component.BookCover
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.BookCover
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.Spacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookGrid(
    books: ImmutableList<Book>,
    onBookClick: (String) -> Unit,
    onBookLongPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Md),
        verticalArrangement = Arrangement.spacedBy(Spacing.Lg)
    ) {
        items(
            items = books,
            key = { it.id }
        ) { book ->
            BookGridItem(
                book = book,
                onClick = { onBookClick(book.id) },
                onLongPress = { onBookLongPress(book.id) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BookGridItem(
    book: Book,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(BookCover.Width)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BookCover(
            gradientStart = book.coverGradientStart,
            gradientEnd = book.coverGradientEnd
        )

        Spacer(modifier = Modifier.height(Spacing.Sm))

        Text(
            text = book.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Foreground,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(BookCover.Width)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookGridPreview() {
    AppTheme {
        BookGrid(
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
                ),
                Book(
                    id = "2",
                    title = "流浪地球",
                    coverGradientStart = "#4facfe",
                    coverGradientEnd = "#00f2fe",
                    lastReadAt = null,
                    readingProgress = 0f,
                    currentChapter = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                Book(
                    id = "3",
                    title = "这是一个很长很长的书名",
                    coverGradientStart = "#fa709a",
                    coverGradientEnd = "#fee140",
                    lastReadAt = null,
                    readingProgress = 0f,
                    currentChapter = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            ),
            onBookClick = {},
            onBookLongPress = {}
        )
    }
}
