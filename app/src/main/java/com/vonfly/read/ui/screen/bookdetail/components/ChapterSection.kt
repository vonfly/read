package com.vonfly.read.ui.screen.bookdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.Chapter
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.ForegroundTertiary
import com.vonfly.read.ui.theme.Radius
import com.vonfly.read.ui.theme.Spacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ChapterSection(
    totalChapters: Int,
    chapters: ImmutableList<Chapter>,
    onChapterClick: (String) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onViewAllClick),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "目录 · 共${totalChapters}章",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Foreground
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "查看全部",
                    fontSize = 14.sp,
                    color = ForegroundSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "查看全部",
                    modifier = Modifier.size(16.dp),
                    tint = ForegroundSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Md))

        // Chapter List (show first 2)
        chapters.take(2).forEach { chapter ->
            ChapterItem(
                chapter = chapter,
                onClick = { onChapterClick(chapter.id) }
            )
            Spacer(modifier = Modifier.height(Spacing.Sm))
        }
    }
}

@Composable
private fun ChapterItem(
    chapter: Chapter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(Radius.Sm))
            .background(Card)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.Lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = chapter.title,
            fontSize = 14.sp,
            color = if (chapter.isFree) Foreground else ForegroundTertiary,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        if (!chapter.isFree) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "付费章节",
                modifier = Modifier.size(16.dp),
                tint = ForegroundTertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChapterSectionPreview() {
    AppTheme {
        ChapterSection(
            totalChapters = 256,
            chapters = persistentListOf(
                Chapter(
                    id = "1",
                    bookId = "book1",
                    title = "第1章 科学边界",
                    index = 0,
                    isFree = true
                ),
                Chapter(
                    id = "2",
                    bookId = "book1",
                    title = "第2章 三体",
                    index = 1,
                    isFree = false
                )
            ),
            onChapterClick = {},
            onViewAllClick = {}
        )
    }
}
