package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.screen.reader.ReaderPanel
import com.vonfly.read.ui.theme.Accent
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundTertiary

/**
 * 阅读器底部控制栏
 *
 * 设计规格来自 Pencil 设计稿 Read-Control-Bar 页面：
 * - 面板高度: 132dp
 * - 顶部圆角: 16dp
 * - 背景色: #F9F9F9
 * - 顶部边框: 1px, 颜色 #E8E8E8
 * - 顶部 padding: 16dp（边框上方）
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
    activeButton: ReaderPanel? = null,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0f) }

    // 同步外部页码
    if (totalPages > 1) {
        progress = (currentPage - 1).toFloat() / (totalPages - 1)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
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
            .background(Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {

            // 进度条区域 - 设计稿 padding: [0, 20, 16, 20], 高度 50dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)  // 设计稿：固定高度 50dp
                    .padding(start = 20.dp, end = 20.dp),
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
                isSelected = activeButton == ReaderPanel.CATALOG,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                icon = Icons.Outlined.WbSunny,
                label = "亮度",
                onClick = onBrightnessClick,
                isSelected = activeButton == ReaderPanel.BRIGHTNESS,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                icon = Icons.Outlined.Abc,
                label = "字体",
                onClick = onFontClick,
                isSelected = activeButton == ReaderPanel.FONT,
                modifier = Modifier.weight(1f)
            )

            ControlButton(
                icon = Icons.Outlined.MoreHoriz,
                label = "更多",
                onClick = onMoreClick,
                isSelected = activeButton == ReaderPanel.MORE,
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
 *
 * 设计规格：
 * - 按钮尺寸: 30×30dp
 * - 圆角: 20dp（接近圆形）
 * - 图标尺寸: 24×24dp
 */
@Composable
private fun PageButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .size(30.dp)  // 设计稿：30×30dp
            .clip(RoundedCornerShape(20.dp))  // 设计稿：圆角 20dp
            .clickable(onClick = onClick, enabled = enabled),
        contentAlignment = Alignment.Center
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
 *
 * 设计规格来自 Pencil 设计稿 BottomControls 组件：
 * - 按钮高度: 66dp (与容器相同)
 * - 按钮宽度: fill_container (平均分配)
 * - 整个按钮区域可点击
 * - 图标尺寸: 22×22dp
 * - 图标和文字间距: 4dp
 * - 文字字号: 10sp
 */
@Composable
private fun ControlButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(66.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Accent else Foreground,
                modifier = Modifier.size(22.dp)
            )

            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Accent else ForegroundTertiary
            )
        }
    }
}

/**
 * 底部控制按钮行（可复用组件）
 *
 * 只包含目录、亮度、字体、更多四个按钮，用于在面板中复用。
 * 设计规格：
 * - 高度: 66dp
 * - 水平内边距: 20dp
 * - 按钮平均分配空间
 */
@Composable
fun BottomControlButtons(
    onCatalogClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onFontClick: () -> Unit,
    onMoreClick: () -> Unit,
    activeButton: ReaderPanel? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
            isSelected = activeButton == ReaderPanel.CATALOG,
            modifier = Modifier.weight(1f)
        )

        ControlButton(
            icon = Icons.Outlined.WbSunny,
            label = "亮度",
            onClick = onBrightnessClick,
            isSelected = activeButton == ReaderPanel.BRIGHTNESS,
            modifier = Modifier.weight(1f)
        )

        ControlButton(
            icon = Icons.Outlined.Abc,
            label = "字体",
            onClick = onFontClick,
            isSelected = activeButton == ReaderPanel.FONT,
            modifier = Modifier.weight(1f)
        )

        ControlButton(
            icon = Icons.Outlined.MoreHoriz,
            label = "更多",
            onClick = onMoreClick,
            isSelected = activeButton == ReaderPanel.MORE,
            modifier = Modifier.weight(1f)
        )
    }
}
