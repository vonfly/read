package com.vonfly.read.ui.screen.bookmark

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.Bookmark
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Background
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.ForegroundTertiary
import com.vonfly.read.ui.theme.Primary
import com.vonfly.read.ui.theme.Spacing
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// 阴影颜色: 7% 不透明度黑色（与 Profile 页面一致）
private val CardShadowColor = Color(0x12000000)

@Composable
fun BookmarkContent(
    uiState: BookmarkUiState,
    onNavigateBack: () -> Unit,
    onBookmarkClick: (Bookmark) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header - 高度 44dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    modifier = Modifier.size(24.dp),
                    tint = Foreground
                )
            }
            Text(
                text = "我的书签",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Foreground
            )
            Spacer(modifier = Modifier.size(24.dp))
        }

        // Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (uiState.bookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = ForegroundTertiary
                    )
                    Text(
                        text = "暂无书签",
                        fontSize = 16.sp,
                        color = ForegroundTertiary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = uiState.bookmarks,
                    key = { it.id }
                ) { bookmark ->
                    BookmarkCard(
                        bookmark = bookmark,
                        onClick = { onBookmarkClick(bookmark) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarkCard(
    bookmark: Bookmark,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .graphicsLayer {
                shadowElevation = 8f
                shape = cardShape
                spotShadowColor = CardShadowColor
                ambientShadowColor = CardShadowColor
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = cardShape,
        color = Card,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 封面渐变
            Box(
                modifier = Modifier
                    .size(width = 52.dp, height = 72.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.linearGradient(bookmark.getCoverGradient())
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 书签信息
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
            ) {
                // 书名
                Text(
                    text = bookmark.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Foreground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 作者·章节
                Text(
                    text = "${bookmark.author} · 第${bookmark.chapter}章",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = ForegroundTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 内容预览
                Text(
                    text = bookmark.preview,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = ForegroundSecondary,
                    lineHeight = 13.sp * 1.3f,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 时间
                Text(
                    text = formatRelativeTime(bookmark.createdAt),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = ForegroundTertiary
                )
            }
        }
    }
}

/**
 * 格式化相对时间
 */
@Composable
private fun formatRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < DateUtils.MINUTE_IN_MILLIS -> "刚刚"
        diff < DateUtils.HOUR_IN_MILLIS -> "${diff / DateUtils.MINUTE_IN_MILLIS} 分钟前"
        diff < DateUtils.DAY_IN_MILLIS -> "${diff / DateUtils.HOUR_IN_MILLIS} 小时前"
        diff < DateUtils.DAY_IN_MILLIS * 7 -> "${diff / DateUtils.DAY_IN_MILLIS} 天前"
        else -> {
            // 超过 7 天显示具体日期
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            sdf.format(java.util.Date(timestamp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkContentPreview() {
    AppTheme {
        BookmarkContent(
            uiState = BookmarkUiState(
                isLoading = false,
                bookmarks = persistentListOf(
                    Bookmark(
                        id = "1",
                        bookId = "book_1",
                        title = "三体",
                        author = "刘慈欣",
                        chapter = 12,
                        preview = "汪淼看着那个幽灵倒计时，时间在不停地闪烁。",
                        coverGradientColors = listOf(0xFF11998e, 0xFF38ef7d),
                        createdAt = System.currentTimeMillis() - 86400000
                    ),
                    Bookmark(
                        id = "2",
                        bookId = "book_2",
                        title = "百年孤独",
                        author = "加西亚·马尔克斯",
                        chapter = 5,
                        preview = "多年以后，面对行刑队，奥雷里亚诺·布恩迪亚上校将会回想起父亲带他去见识冰块的那个遥远的下午。",
                        coverGradientColors = listOf(0xFF667eea, 0xFF764ba2),
                        createdAt = System.currentTimeMillis() - 172800000
                    )
                )
            ),
            onNavigateBack = {},
            onBookmarkClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkContentEmptyPreview() {
    AppTheme {
        BookmarkContent(
            uiState = BookmarkUiState(
                isLoading = false,
                bookmarks = persistentListOf()
            ),
            onNavigateBack = {},
            onBookmarkClick = {}
        )
    }
}
