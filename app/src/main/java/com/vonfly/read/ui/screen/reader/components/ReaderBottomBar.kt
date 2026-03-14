package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.theme.Accent
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundTertiary

/**
 * 阅读器底部控制栏
 *
 * 设计规格来自 Pencil 设计稿 Read-Control-Bar 页面：
 * - 面板高度: 122dp
 * - 顶部圆角: 16dp
 * - 背景色: #F9F9F9
 * - 阴影: blur 20, color #00000025 (15%), offset (0, 4)
 * - 顶部边框: 1px, 颜色 #E8E8E8
 */
@Composable
fun ReaderBottomBar(
    currentPage: Int,
    totalPages: Int,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit,
    onProgressChange: (Float) -> Unit,
    onCatalogClick: () -> Unit,
    onFontClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0f) }

    // 同步外部页码
    if (totalPages > 1) {
        progress = (currentPage - 1).toFloat() / (totalPages - 1)
    }

    // 设计稿规格
    val cornerRadius = 16.dp
    val shadowColor = Color(0x25000000) // 15% 不透明度
    val bgColor = Color(0xFFF9F9F9)
    val borderColor = Color(0xFFE8E8E8)

    // 使用 Surface 组件来正确处理阴影和圆角
    androidx.compose.material3.Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(122.dp),
        shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius),
        color = bgColor,
        shadowElevation = 20.dp,
        tonalElevation = 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 顶部边框线 - 1dp 高度，颜色 #E8E8E8
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(borderColor)
            )

            // 进度条区域 - 设计稿 padding: [0, 20, 16, 20]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            // 上一页按钮
            PageButton(
                icon = Icons.Default.ChevronLeft,
                onClick = onPreviousPage,
                enabled = currentPage > 1
            )

            // 进度条
            CustomProgressBar(
                progress = progress,
                onProgressChange = { newProgress ->
                    progress = newProgress
                    onProgressChange(newProgress)
                },
                modifier = Modifier.weight(1f)
            )

            // 下一页按钮
            PageButton(
                icon = Icons.Default.ChevronRight,
                onClick = onNextPage,
                enabled = currentPage < totalPages
            )
        }

        // 底部控制按钮 - 高度 66dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlButton(
                icon = Icons.AutoMirrored.Filled.List,
                label = "目录",
                onClick = onCatalogClick,
                isSelected = false,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                icon = Icons.Outlined.WbSunny,
                label = "亮度",
                onClick = onBrightnessClick,
                isSelected = false,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                icon = Icons.Outlined.Abc,
                label = "字体",
                onClick = onFontClick,
                isSelected = false,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                icon = Icons.Outlined.MoreHoriz,
                label = "更多",
                onClick = onMoreClick,
                isSelected = false,
                modifier = Modifier.weight(1f)
            )
        }
        } // 关闭 Column
    } // 关闭 Surface
}

/**
 * 自定义进度条
 *
 * 设计规格来自 Pencil 设计稿 ProgressBar 组件：
 * - 轨道高度: 28dp, 圆角 14dp
 * - 轨道背景色: #E5E5EA
 * - 已读部分: 高度 28dp, 圆角 14dp, 颜色 #C5C5CA, 阴影 blur 4
 * - 滑块: 24×24dp, 圆角 12dp (圆形), 白色 #FFFFFF, y=2dp
 * - 滑块阴影: blur 2, color #00000025, offset (0, 1)
 */
@Composable
private fun CustomProgressBar(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var trackWidth by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    // 设计稿规格
    val trackHeight = 28.dp
    val thumbSize = 24.dp
    val thumbSizePx = with(density) { thumbSize.toPx() }

    // 颜色
    val trackColor = Color(0xFFE5E5EA)       // 轨道背景
    val activeColor = Color(0xFFC5C5CA)      // 已读部分
    val thumbColor = Color.White             // 滑块
    val thumbShadowColor = Color(0x25000000) // 滑块阴影 15%

    Box(
        modifier = modifier
            .height(trackHeight)
            .onSizeChanged { size: IntSize ->
                trackWidth = size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    if (trackWidth > thumbSizePx) {
                        val newX = (change.position.x - thumbSizePx / 2).coerceIn(0f, trackWidth - thumbSizePx)
                        val newProgress = newX / (trackWidth - thumbSizePx)
                        onProgressChange(newProgress.coerceIn(0f, 1f))
                    }
                }
            }
    ) {
        // 轨道背景 - #E5E5EA, 圆角 14dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(RoundedCornerShape(14.dp))
                .background(trackColor)
        )

        // 已读部分 - #C5C5CA, 圆角 14dp
        // 设计稿：已读宽度 = 轨道宽度 × 进度
        // 最小宽度为 26dp，确保滑块始终在已读部分上
        val minWidthPx = with(density) { 26.dp.toPx() }
        val readWidth = (trackWidth * progress).coerceAtLeast(minWidthPx)
        Box(
            modifier = Modifier
                .width(with(density) { readWidth.toDp() })
                .height(trackHeight)
                .clip(RoundedCornerShape(14.dp))
                .background(activeColor)
        )

        // 滑块 - 白色圆形, 24×24dp, y=2dp (垂直居中)
        // 设计稿：滑块左边缘对齐已读部分右边缘，再向左偏移 1dp
        // thumbOffsetX = readWidth - thumbSize - 1dp
        // 当 readWidth = 26dp, thumbSize = 24dp 时：左边缘 = 26-24-1 = 1dp
        val offset1dp = with(density) { 1.dp.toPx() }
        val minThumbOffsetPx = offset1dp
        val maxOffset = (trackWidth - thumbSizePx).coerceAtLeast(minThumbOffsetPx)
        val thumbOffsetX = if (maxOffset > minThumbOffsetPx) {
            // 滑块在已读部分右边缘，向左偏移 1dp
            (readWidth - thumbSizePx - offset1dp).coerceIn(minThumbOffsetPx, maxOffset)
        } else {
            minThumbOffsetPx
        }
        Box(
            modifier = Modifier
                .offset(
                    x = with(density) { thumbOffsetX.toDp() },
                    y = 2.dp  // 设计稿 y=2，让 24dp 滑块在 28dp 轨道中垂直居中
                )
                .size(thumbSize)
                .shadow(
                    elevation = 2.dp,
                    shape = CircleShape,
                    ambientColor = thumbShadowColor,
                    spotColor = thumbShadowColor
                )
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

/**
 * 翻页按钮
 */
@Composable
private fun PageButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (enabled) Foreground else Color(0xFFCCCCCC)
        )
    }
}

/**
 * 底部控制按钮
 */
@Composable
private fun ControlButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(66.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(22.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Accent else ForegroundTertiary,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Accent else ForegroundTertiary
        )
    }
}
