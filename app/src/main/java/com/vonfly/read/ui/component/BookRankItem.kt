package com.vonfly.read.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.StoreBook
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.ForegroundTertiary

/**
 * 书籍排行项组件
 *
 * 用于书城热门榜单列表项展示。
 *
 * @param book 书籍数据
 * @param onClick 点击回调
 * @param modifier Modifier
 */
@Composable
fun BookRankItem(
    book: StoreBook,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = Color(0xFFFF6B6B)
    val startColor = Color(android.graphics.Color.parseColor(book.coverGradientStart))
    val endColor = Color(android.graphics.Color.parseColor(book.coverGradientEnd))
    val coverWidth = 48.dp
    val coverHeight = 64.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Card)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排名数字
        Text(
            text = book.rank.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor,
            modifier = Modifier.width(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 书籍封面（小尺寸渐变方块）
        Box(
            modifier = Modifier
                .size(coverWidth, coverHeight)
                .clip(RoundedCornerShape(4.dp))
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(4.dp),
                    ambientColor = Color(0x10000000),
                    spotColor = Color(0x10000000)
                )
                .background(
                    Brush.linearGradient(
                        colors = listOf(startColor, endColor),
                        start = Offset(0f, 0f),
                        end = Offset(coverWidth.value, coverHeight.value)
                    )
                )
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 书籍信息
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = book.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Foreground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.author,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = ForegroundSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${book.rating}分 · ${book.readCount}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = ForegroundTertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookRankItemPreview() {
    BookRankItem(
        book = StoreBook(
            id = "1",
            rank = 1,
            title = "三体",
            author = "刘慈欣",
            rating = 9.4f,
            readCount = "892万人读过",
            coverGradientStart = "#667eea",
            coverGradientEnd = "#764ba2"
        ),
        onClick = {}
    )
}
