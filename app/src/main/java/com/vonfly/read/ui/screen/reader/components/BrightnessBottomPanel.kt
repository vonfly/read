package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.ui.screen.reader.ReaderPanel
import com.vonfly.read.ui.theme.Accent
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundTertiary

/**
 * 亮度底部面板组件
 *
 * 设计规格来自 Pencil 设计稿 Read-Brightness 页面：
 * - 面板高度: 272dp
 * - 顶部圆角: 16dp
 * - 背景色: #F9F9F9
 * - 阴影: blur=20, color=#00000025
 * - 顶部边框: 1dp, #E8E8E8
 */
@Composable
fun BrightnessBottomPanel(
    brightness: Float,
    currentColorScheme: ReaderColorScheme,
    onBrightnessChange: (Float) -> Unit,
    onColorSchemeChange: (ReaderColorScheme) -> Unit,
    onCatalogClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onFontClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(272.dp)
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
                                android.graphics.Color.argb(37, 0, 0, 0)  // #00000025
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
        // 顶部内边距
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            // 亮度调节区 - 高度 90dp
            BrightnessSection(
                brightness = brightness,
                currentColorScheme = currentColorScheme,
                onBrightnessChange = onBrightnessChange
            )

            // 背景选择区 - 高度 100dp
            BackgroundSection(
                currentColorScheme = currentColorScheme,
                onColorSchemeChange = onColorSchemeChange
            )

            // 底部控制按钮 - 高度 66dp
            BottomControlButtons(
                onCatalogClick = onCatalogClick,
                onBrightnessClick = onBrightnessClick,
                onFontClick = onFontClick,
                onMoreClick = onMoreClick,
                activeButton = ReaderPanel.BRIGHTNESS,
                currentColorScheme = currentColorScheme
            )
        }
    }
}

/**
 * 亮度调节区
 *
 * 设计规格：
 * - 高度: 90dp
 * - 内边距: vertical: 8dp, horizontal: 20dp
 * - 间距: 12dp
 */
@Composable
private fun BrightnessSection(
    brightness: Float,
    currentColorScheme: ReaderColorScheme,
    onBrightnessChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 8.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题
        Text(
            text = "亮度",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = currentColorScheme.text
        )

        // 滑块行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 小太阳图标
            Icon(
                imageVector = Icons.Outlined.WbSunny,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = currentColorScheme.text.copy(alpha = 0.5f)
            )

            // 亮度滑块
            BrightnessSlider(
                brightness = brightness,
                currentColorScheme = currentColorScheme,
                onBrightnessChange = onBrightnessChange,
                modifier = Modifier.weight(1f)
            )

            // 大太阳图标
            Icon(
                imageVector = Icons.Filled.WbSunny,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = currentColorScheme.text
            )
        }
    }
}

/**
 * 亮度滑块
 *
 * 复用 CustomProgressBar 样式，但作为内部组件实现。
 * 设计规格：
 * - 轨道高度: 28dp, 圆角 14dp
 * - 滑块: 24x24dp, 圆形, 白色 #FFFFFF
 */
@Composable
private fun BrightnessSlider(
    brightness: Float,
    currentColorScheme: ReaderColorScheme,
    onBrightnessChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var trackWidth by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    // 设计稿规格
    val trackHeight = 28.dp
    val thumbSize = 24.dp
    val thumbSizePx = with(density) { thumbSize.toPx() }

    // 从主题获取颜色
    val trackColor = currentColorScheme.sliderTrack
    val activeColor = currentColorScheme.sliderActive
    val thumbColor = Color.White
    val thumbBorderColor = currentColorScheme.sliderThumbBorder

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
                        onBrightnessChange(newProgress.coerceIn(0f, 1f))
                    }
                }
            }
    ) {
        // 轨道背景
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .clip(RoundedCornerShape(14.dp))
                .background(trackColor)
        )

        // 已读部分
        Box(
            modifier = Modifier
                .fillMaxWidth(brightness)
                .height(trackHeight)
                .clip(RoundedCornerShape(14.dp))
                .background(activeColor)
        )

        // 滑块 - 参考 CustomProgressBar 的定位逻辑
        val minWidthPx = with(density) { 26.dp.toPx() }
        val readWidth = (trackWidth * brightness).coerceAtLeast(minWidthPx)
        val offset1dp = with(density) { 1.dp.toPx() }
        val minThumbOffsetPx = offset1dp
        val maxOffset = (trackWidth - thumbSizePx).coerceAtLeast(minThumbOffsetPx)
        val thumbOffsetX = if (maxOffset > minThumbOffsetPx) {
            (readWidth - thumbSizePx - offset1dp).coerceIn(minThumbOffsetPx, maxOffset)
        } else {
            minThumbOffsetPx
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = with(density) { thumbOffsetX.toDp() })
                .size(thumbSize)
                .border(1.dp, thumbBorderColor, CircleShape)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

/**
 * 背景选择区
 *
 * 设计规格：
 * - 高度: 100dp
 * - 内边距: vertical: 8dp, horizontal: 20dp
 * - 间距: 12dp
 */
@Composable
private fun BackgroundSection(
    currentColorScheme: ReaderColorScheme,
    onColorSchemeChange: (ReaderColorScheme) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题
        Text(
            text = "背景",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Foreground
        )

        // 颜色选项行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 6 种颜色选项
            ColorOption(
                color = ReaderColorScheme.Default.background,
                isSelected = currentColorScheme == ReaderColorScheme.Default,
                onClick = { onColorSchemeChange(ReaderColorScheme.Default) }
            )

            ColorOption(
                color = ReaderColorScheme.Sepia.background,
                isSelected = currentColorScheme == ReaderColorScheme.Sepia,
                onClick = { onColorSchemeChange(ReaderColorScheme.Sepia) }
            )

            ColorOption(
                color = ReaderColorScheme.Pink.background,
                isSelected = currentColorScheme == ReaderColorScheme.Pink,
                onClick = { onColorSchemeChange(ReaderColorScheme.Pink) }
            )

            ColorOption(
                color = ReaderColorScheme.Green.background,
                isSelected = currentColorScheme == ReaderColorScheme.Green,
                onClick = { onColorSchemeChange(ReaderColorScheme.Green) }
            )

            ColorOption(
                color = ReaderColorScheme.Night.background,
                isSelected = currentColorScheme == ReaderColorScheme.Night,
                onClick = { onColorSchemeChange(ReaderColorScheme.Night) }
            )

            ColorOption(
                color = ReaderColorScheme.Cream.background,
                isSelected = currentColorScheme == ReaderColorScheme.Cream,
                onClick = { onColorSchemeChange(ReaderColorScheme.Cream) }
            )
        }
    }
}

/**
 * 颜色选项
 *
 * 设计规格：
 * - 尺寸: 40x40dp
 * - 圆角: 20dp (圆形)
 * - 选中状态: 边框 2dp Accent, 显示勾选图标 16dp
 * - 未选中状态: 边框 1dp #E0E0E0
 */
@Composable
private fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, Accent, CircleShape)
                } else {
                    Modifier.border(1.dp, Color(0xFFE0E0E0), CircleShape)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Accent
            )
        }
    }
}
