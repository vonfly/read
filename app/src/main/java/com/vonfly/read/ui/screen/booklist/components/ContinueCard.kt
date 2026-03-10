package com.vonfly.read.ui.screen.booklist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.Book
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.Radius
import com.vonfly.read.ui.theme.Spacing
import kotlin.math.roundToInt

@Composable
fun ContinueCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressPercent = (book.readingProgress * 100).roundToInt()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(Radius.Md))
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(Radius.Md),
                ambientColor = Color(0x08000000),
                spotColor = Color(0x08000000)
            )
            .background(Card)
            .clickable(onClick = onClick)
            .padding(Spacing.Md)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Md)
        ) {
            // Cover
            ContinueCardCover(
                gradientStart = book.coverGradientStart,
                gradientEnd = book.coverGradientEnd
            )

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Foreground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Spacing.Xs))

                Text(
                    text = buildProgressText(progressPercent, book.currentChapter),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = ForegroundSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Spacing.Sm))

                // Progress Bar
                ProgressBar(
                    progress = book.readingProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ContinueCardCover(
    gradientStart: String,
    gradientEnd: String
) {
    val startColor = Color(android.graphics.Color.parseColor(gradientStart))
    val endColor = Color(android.graphics.Color.parseColor(gradientEnd))

    Box(
        modifier = Modifier
            .size(width = 56.dp, height = 76.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                )
            )
    )
}

@Composable
private fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(com.vonfly.read.ui.theme.SliderTrack)
    ) {
        Box(
            modifier = Modifier
                .weight(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(com.vonfly.read.ui.theme.SliderActive)
        )
        Spacer(modifier = Modifier.weight((1f - progress).coerceIn(0f, 1f)))
    }
}

@Composable
private fun buildProgressText(percent: Int, chapter: String?): String {
    val percentText = "已读 $percent%"
    return if (chapter != null) {
        "$percentText · $chapter"
    } else {
        percentText
    }
}

@Preview(showBackground = true)
@Composable
private fun ContinueCardPreview() {
    AppTheme {
        ContinueCard(
            book = Book(
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
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContinueCardLongTitlePreview() {
    AppTheme {
        ContinueCard(
            book = Book(
                id = "1",
                title = "这是一个非常长的书名用于测试省略号效果",
                coverGradientStart = "#4facfe",
                coverGradientEnd = "#00f2fe",
                lastReadAt = System.currentTimeMillis(),
                readingProgress = 0.75f,
                currentChapter = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
            onClick = {}
        )
    }
}
