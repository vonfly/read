package com.vonfly.read.ui.screen.bookdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.component.BookCover
import com.vonfly.read.ui.theme.Accent
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary

@Composable
fun BookInfoSection(
    title: String,
    author: String,
    coverGradientStart: String,
    coverGradientEnd: String,
    rating: Float,
    readerCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Book Cover
        BookCover(
            gradientStart = coverGradientStart,
            gradientEnd = coverGradientEnd
        )

        // Book Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Foreground,
                maxLines = 2
            )

            // Author
            Text(
                text = author,
                fontSize = 14.sp,
                color = ForegroundSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rating and Reader Count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "评分",
                        modifier = Modifier.size(16.dp),
                        tint = Accent
                    )
                    Text(
                        text = String.format("%.1f", rating),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Foreground
                    )
                }

                // Reader Count
                Text(
                    text = formatReaderCount(readerCount),
                    fontSize = 14.sp,
                    color = ForegroundSecondary
                )
            }
        }
    }
}

@Composable
private fun formatReaderCount(count: Int): String {
    return when {
        count >= 10000 -> String.format("%.1f万人", count / 10000.0)
        count >= 1000 -> String.format("%.1fk人", count / 1000.0)
        else -> "${count}人"
    } + "在读"
}

@Preview(showBackground = true)
@Composable
private fun BookInfoSectionPreview() {
    AppTheme {
        BookInfoSection(
            title = "三体",
            author = "刘慈欣",
            coverGradientStart = "#667eea",
            coverGradientEnd = "#764ba2",
            rating = 9.4f,
            readerCount = 125680
        )
    }
}
