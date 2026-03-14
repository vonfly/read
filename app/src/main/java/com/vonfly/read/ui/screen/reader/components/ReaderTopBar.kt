package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material.icons.outlined.Settings
import com.vonfly.read.ui.theme.Accent
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary

/**
 * 阅读器顶部控制栏
 *
 * 设计规格来自 Pencil 设计稿 Read-Control-Bar 页面：
 * - TopBar 高度: 72dp
 * - 背景色: #F9F9F9CC (80% 不透明度)
 * - 背景模糊: blur radius 20
 * - 水平内边距: 20dp
 * - 返回按钮尺寸: 24×24dp
 * - 书名字号: 17sp
 * - 书名字重: 600 (SemiBold)
 * - 书名颜色: $--foreground
 * - 操作按钮间距: 24dp
 * - 操作按钮图标尺寸: 22×22dp
 */
@Composable
fun ReaderTopBar(
    bookTitle: String,
    onNavigateBack: () -> Unit,
    onAddToShelfClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    isInShelf: Boolean = false,
    isBookmarked: Boolean = false
) {
    // 设计稿背景色: #F9F9F9 (实色)
    val backgroundColor = Color(0xFFF9F9F9)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(72.dp)
            .drawBehind {
                drawRect(color = backgroundColor)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮 - 24×24 dp, 颜色: Foreground
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = Foreground,
                    modifier = Modifier.size(24.dp)
                )
            }

            // 书名 - 17 sp, SemiBold, Foreground
            Text(
                text = bookTitle,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Foreground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            // 操作按钮组 - 间距 24 dp
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 加入书架 - Material 图标: LibraryAdd
                // 颜色: 已加入书架时 Accent，未加入时 ForegroundSecondary
                ActionButton(
                    icon = Icons.Outlined.LibraryAdd,
                    contentDescription = "加入书架",
                    onClick = onAddToShelfClick,
                    tint = if (isInShelf) Accent else ForegroundSecondary
                )

                // 书签 - Material 图标: BookmarkBorder / Bookmark
                // 颜色: 已添加书签时 Accent，未添加时 ForegroundSecondary
                ActionButton(
                    icon = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "书签",
                    onClick = onBookmarkClick,
                    tint = if (isBookmarked) Accent else ForegroundSecondary
                )

                // 设置 - Material 图标: Settings, 颜色: ForegroundSecondary
                ActionButton(
                    icon = Icons.Outlined.Settings,
                    contentDescription = "设置",
                    onClick = onSettingsClick,
                    tint = ForegroundSecondary
                )
            }
        }
    }
}

/**
 * 操作按钮
 *
 * 设计规格：
 * - 图标尺寸: 22×22 dp
 */
@Composable
private fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(22.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}
