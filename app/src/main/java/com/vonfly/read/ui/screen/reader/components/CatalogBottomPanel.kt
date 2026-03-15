package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.Chapter
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.ui.screen.reader.ReaderPanel
import com.vonfly.read.ui.theme.Success
import kotlinx.collections.immutable.ImmutableList

/**
 * 目录底部面板组件
 *
 * 用于阅读器底部面板的目录模式，原地切换显示。
 * 设计规格来自 Pencil 设计稿 Read-Contents 页面：
 * - 面板高度: 屏幕高度的 90%（适配不同屏幕尺寸）
 * - 顶部圆角: 16dp
 * - 背景色: 随主题变化 (默认 #F9F9F9)
 * - 阴影: blur=20, color=#00000025
 */
@Composable
fun CatalogBottomPanel(
    chapters: ImmutableList<Chapter>,
    currentChapterIndex: Int,
    isSortAscending: Boolean,
    currentColorScheme: ReaderColorScheme,
    onChapterClick: (Int) -> Unit,
    onToggleSortOrder: () -> Unit,
    onCatalogClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onFontClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sortedChapters = if (isSortAscending) {
        chapters
    } else {
        chapters.reversed()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)  // 面板高度为屏幕高度的 90%，适配不同屏幕尺寸
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        asFrameworkPaint().apply {
                            isAntiAlias = true
                            color = android.graphics.Color.TRANSPARENT
                            setShadowLayer(
                                20f,   // blur radius (设计稿 blur=20)
                                0f,    // dx
                                -4f,   // dy 负数 = 向上
                                android.graphics.Color.argb(37, 0, 0, 0)  // #00000025 ≈ 15%
                            )
                        }
                    }
                    canvas.drawRoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = 16.dp.toPx(),
                        radiusY = 16.dp.toPx(),
                        paint = paint
                    )
                }
            }
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(currentColorScheme.panelBackground)
    ) {
        // Header - 高度 56dp
        CatalogHeader(
            isSortAscending = isSortAscending,
            currentColorScheme = currentColorScheme,
            onToggleSortOrder = onToggleSortOrder
        )

        // 章节列表 - 占据剩余空间
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(
                items = sortedChapters,
                key = { it.id }
            ) { chapter ->
                val originalIndex = chapter.index
                val isCurrentChapter = originalIndex == currentChapterIndex

                ChapterItem(
                    chapter = chapter,
                    isCurrentChapter = isCurrentChapter,
                    currentColorScheme = currentColorScheme,
                    onClick = { onChapterClick(originalIndex) }
                )
            }
        }

        // 底部控制按钮 - 复用 BottomControlButtons
        BottomControlButtons(
            onCatalogClick = onCatalogClick,
            onBrightnessClick = onBrightnessClick,
            onFontClick = onFontClick,
            onMoreClick = onMoreClick,
            activeButton = ReaderPanel.CATALOG,
            currentColorScheme = currentColorScheme
        )
    }
}

/**
 * 目录 Header
 *
 * 设计规格：
 * - 高度: 56dp
 * - 水平内边距: 20dp
 * - 底部边框: 1dp, #F0F0F0（亮色主题）/ #3A3A3A（深色主题）
 * - 标题: 18sp, SemiBold, 随主题变化
 * - 排序按钮: 图标 16dp + 文字 14sp, 随主题变化（60% 透明度）
 */
@Composable
private fun CatalogHeader(
    isSortAscending: Boolean,
    currentColorScheme: ReaderColorScheme,
    onToggleSortOrder: () -> Unit
) {
    // 边框颜色：深色主题使用深色边框，亮色主题使用浅色边框
    val dividerColor = if (currentColorScheme == ReaderColorScheme.Night) {
        Color(0xFF3A3A3A)
    } else {
        Color(0xFFF0F0F0)
    }
    // 次要文字颜色：使用主题文字颜色的 60% 透明度
    val secondaryTextColor = currentColorScheme.text.copy(alpha = 0.6f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 标题
            Text(
                text = "目录",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = currentColorScheme.text
            )

            // 排序按钮
            Row(
                modifier = Modifier.clickable(onClick = onToggleSortOrder),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isSortAscending) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = secondaryTextColor
                )
                Text(
                    text = if (isSortAscending) "正序" else "倒序",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = secondaryTextColor
                )
            }
        }

        // 底部边框
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = dividerColor
        )
    }
}

/**
 * 章节项
 *
 * 设计规格：
 * - 高度: 48dp
 * - 水平内边距: 20dp
 * - 标题字号: 15sp
 * - 当前章节背景: 主题背景色 50% 透明度
 * - 当前章节标题: 主题文字色, Medium
 * - 非当前章节标题: 主题文字色 60% 透明度, Normal
 * - 免费标签: Success, 12sp, 500
 * - 锁图标: 14dp, 主题文字色 40% 透明度
 */
@Composable
private fun ChapterItem(
    chapter: Chapter,
    isCurrentChapter: Boolean,
    currentColorScheme: ReaderColorScheme,
    onClick: () -> Unit
) {
    // 当前章节使用主题色高亮，其他章节透明背景
    val backgroundColor = if (isCurrentChapter) {
        currentColorScheme.background.copy(alpha = 0.5f)
    } else {
        Color.Transparent
    }
    // 所有章节都使用主题文字色，非当前章节降低透明度
    val titleColor = if (isCurrentChapter) {
        currentColorScheme.text
    } else {
        currentColorScheme.text.copy(alpha = 0.6f)
    }
    val titleWeight = if (isCurrentChapter) FontWeight.Medium else FontWeight.Normal
    // 锁图标颜色：使用主题文字色的 40% 透明度
    val lockIconColor = currentColorScheme.text.copy(alpha = 0.4f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 章节标题
        Text(
            text = chapter.title,
            fontSize = 15.sp,
            fontWeight = titleWeight,
            color = titleColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        // 右侧标记
        when {
            isCurrentChapter -> {
                Text(
                    text = "当前",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = currentColorScheme.text
                )
            }
            chapter.isFree -> {
                Text(
                    text = "免费",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Success
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "锁定",
                    modifier = Modifier.size(14.dp),
                    tint = lockIconColor
                )
            }
        }
    }
}
