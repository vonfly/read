package com.vonfly.read.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vonfly.read.ui.theme.BookCover

@Composable
fun BookCover(
    gradientStart: String,
    gradientEnd: String,
    modifier: Modifier = Modifier
) {
    val startColor = Color(android.graphics.Color.parseColor(gradientStart))
    val endColor = Color(android.graphics.Color.parseColor(gradientEnd))

    Box(
        modifier = modifier
            .size(BookCover.Width, BookCover.Height)
            .clip(RoundedCornerShape(BookCover.CornerRadius))
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(BookCover.CornerRadius),
                ambientColor = Color(0x15000000),
                spotColor = Color(0x15000000)
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(startColor, endColor),
                    start = Offset(0f, 0f),
                    end = Offset(
                        BookCover.Width.value,
                        BookCover.Height.value
                    )
                )
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun BookCoverPreview() {
    BookCover(
        gradientStart = "#667eea",
        gradientEnd = "#764ba2"
    )
}
