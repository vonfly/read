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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.ui.screen.reader.ReaderPanel

/**
 * 字体底部面板组件
 *
 * 设计规格来自 Pencil 设计稿 Read-Font 页面：
 * - 面板高度: 272dp
 * - 顶部圆角: 16dp
 * - 背景色: currentColorScheme.panelBackground
 * - 阴影: blur=20, color=#00000025
 */
@Composable
fun FontBottomPanel(
    fontSize: Float,
    lineHeight: Float,
    letterSpacing: Float,
    currentColorScheme: ReaderColorScheme,
    onFontSizeChange: (Float) -> Unit,
    onLineHeightChange: (Float) -> Unit,
    onLetterSpacingChange: (Float) -> Unit,
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
            // 字体大小调节区 - 高度 90dp
            FontSizeSection(
                fontSize = fontSize,
                currentColorScheme = currentColorScheme,
                onFontSizeChange = onFontSizeChange
            )

            // 间距选择区 - 高度 100dp
            SpacingSection(
                lineHeight = lineHeight,
                letterSpacing = letterSpacing,
                currentColorScheme = currentColorScheme,
                onLineHeightChange = onLineHeightChange,
                onLetterSpacingChange = onLetterSpacingChange
            )

            // 底部控制按钮 - 高度 66dp
            BottomControlButtons(
                onCatalogClick = onCatalogClick,
                onBrightnessClick = onBrightnessClick,
                onFontClick = onFontClick,
                onMoreClick = onMoreClick,
                activeButton = ReaderPanel.FONT,
                currentColorScheme = currentColorScheme
            )
        }
    }
}

/**
 * 字体大小调节区
 *
 * 设计规格：
 * - 高度: 90dp
 * - 内边距: vertical: 12dp, horizontal: 20dp
 * - 间距: 12dp
 */
@Composable
private fun FontSizeSection(
    fontSize: Float,
    currentColorScheme: ReaderColorScheme,
    onFontSizeChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题
        Text(
            text = "字体大小",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = currentColorScheme.text
        )

        // 滑块行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 小 "A" 标签（左侧）
            Text(
                text = "A",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = currentColorScheme.text.copy(alpha = 0.5f)
            )

            // 字体大小滑块
            FontSizeSlider(
                fontSize = fontSize,
                currentColorScheme = currentColorScheme,
                onFontSizeChange = onFontSizeChange,
                modifier = Modifier.weight(1f)
            )

            // 大 "A" 标签（右侧）
            Text(
                text = "A",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = currentColorScheme.text.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * 字体大小滑块
 *
 * 复用 CustomProgressBar 样式。
 * 设计规格：
 * - 轨道高度: 28dp, 圆角 14dp
 * - 滑块: 24x24dp, 圆形, 白色 #FFFFFF
 * - 最小已读宽度: 26dp，确保滑块始终在已读部分上
 * - 字体范围: 12sp - 32sp
 */
@Composable
private fun FontSizeSlider(
    fontSize: Float,
    currentColorScheme: ReaderColorScheme,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var trackWidth by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    // 设计稿规格
    val trackHeight = 28.dp
    val thumbSize = 24.dp
    val thumbSizePx = with(density) { thumbSize.toPx() }

    // 字体范围
    val minFontSize = 12f
    val maxFontSize = 32f
    val progress = (fontSize - minFontSize) / (maxFontSize - minFontSize)

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
                        val newFontSize = minFontSize + newProgress * (maxFontSize - minFontSize)
                        onFontSizeChange(newFontSize.coerceIn(minFontSize, maxFontSize))
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

        // 已读部分 - 最小宽度 26dp，确保滑块始终在已读部分上
        val minWidthPx = with(density) { 26.dp.toPx() }
        val readWidth = (trackWidth * progress).coerceAtLeast(minWidthPx)
        Box(
            modifier = Modifier
                .width(with(density) { readWidth.toDp() })
                .height(trackHeight)
                .clip(RoundedCornerShape(14.dp))
                .background(activeColor)
        )

        // 滑块
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
                .offset(x = with(density) { thumbOffsetX.toDp() })
                .size(thumbSize)
                .border(1.dp, thumbBorderColor, CircleShape)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

/**
 * 间距选择区
 *
 * 设计规格：
 * - 高度: 100dp
 * - 内边距: vertical: 12dp, horizontal: 20dp
 * - 间距: 20dp（两个 Group 之间）
 * - 布局: 水平 space_between
 */
@Composable
private fun SpacingSection(
    lineHeight: Float,
    letterSpacing: Float,
    currentColorScheme: ReaderColorScheme,
    onLineHeightChange: (Float) -> Unit,
    onLetterSpacingChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 行间距组
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "行间距",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = currentColorScheme.text
            )

            SegmentedControl(
                options = listOf("1.0", "1.5", "1.8", "2.2"),
                selectedIndex = when (lineHeight) {
                    1.0f -> 0
                    1.5f -> 1
                    1.8f -> 2
                    2.2f -> 3
                    else -> 2  // 默认 1.8
                },
                currentColorScheme = currentColorScheme,
                onOptionSelected = { index ->
                    val values = listOf(1.0f, 1.5f, 1.8f, 2.2f)
                    onLineHeightChange(values[index])
                }
            )
        }

        // 字间距组
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "字间距",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = currentColorScheme.text
            )

            SegmentedControl(
                options = listOf("0", "1", "2", "3"),
                selectedIndex = when (letterSpacing) {
                    0f -> 0
                    1f -> 1
                    2f -> 2
                    3f -> 3
                    else -> 0  // 默认 0
                },
                currentColorScheme = currentColorScheme,
                onOptionSelected = { index ->
                    val values = listOf(0f, 1f, 2f, 3f)
                    onLetterSpacingChange(values[index])
                }
            )
        }
    }
}

/**
 * 分段控制器
 *
 * 设计规格：
 * - 高度: 40dp
 * - 圆角: 12dp
 * - 背景: 根据主题动态调整， * - 内边距: 4dp
 * - 选项高度: 32dp
 * - 选项圆角: 10dp
 * - 选中背景: #FFFFFF + 阴影
 * - 未选中背景: 透明
 */
@Composable
private fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    currentColorScheme: ReaderColorScheme,
    onOptionSelected: (Int) -> Unit
) {
    // 根据主题选择背景色，让 SegmentedControl 与面板背景协调
    val containerColor = when (currentColorScheme) {
        // 深色主题：使用深灰色
        ReaderColorScheme.Night -> Color(0xFF3A3A3A)
        // 暖色调主题：使用与面板背景相近但略深的颜色
        ReaderColorScheme.Sepia -> Color(0xFFE8E0D1)   // 比 #F5F0E1 略深的米色
        ReaderColorScheme.Pink -> Color(0xFFE8D4DA)    // 比 #FFE4E8 略深的粉色
        ReaderColorScheme.Green -> Color(0xFFD4E8D5)   // 比 #E8F5E9 略深的绿色
        ReaderColorScheme.Cream -> Color(0xFFF0E8DC)   // 比 #FDF8F0 略深的奶油色
        // 默认主题：使用浅灰色
        ReaderColorScheme.Default -> Color(0xFFF5F5F7)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex

            // 深色主题下使用不同的选中样式
            val isNightTheme = currentColorScheme == ReaderColorScheme.Night
            val selectedBackgroundColor = if (isNightTheme) {
                Color(0xFF5A5A5A)  // 深色主题：浅灰色，不那么刺眼
            } else {
                Color.White       // 亮色主题：白色
            }
            val selectedTextColor = if (isNightTheme) {
                Color(0xFFE5E5EA)  // 深色主题：浅色文字
            } else {
                currentColorScheme.text  // 亮色主题：跟随主题文字色
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .then(
                        if (isSelected) {
                            // 统一使用更明显的阴影效果
                            Modifier
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    ambientColor = Color(0x35000000),  // 21% 透明度，更明显
                                    spotColor = Color(0x35000000)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(selectedBackgroundColor)
                        } else {
                            Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Transparent)
                        }
                    )
                    .clickable { onOptionSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    color = if (isSelected) {
                        selectedTextColor
                    } else {
                        currentColorScheme.text.copy(alpha = 0.6f)
                    }
                )
            }
        }
    }
}
