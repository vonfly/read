package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.PageTurnMode
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.ui.screen.reader.ReaderPanel
import com.vonfly.read.ui.theme.Accent

/**
 * 更多设置底部面板组件
 *
 * 设计规格来自 Pencil 设计稿 Read-More 页面：
 * - 面板高度: 238dp
 * - 顶部圆角: 16dp
 * - 背景色: currentColorScheme.panelBackground
 * - 阴影: blur=20, color=#00000025
 */
@Composable
fun MoreBottomPanel(
    pageTurnMode: PageTurnMode,
    autoPageEnabled: Boolean,
    currentColorScheme: ReaderColorScheme,
    onPageTurnModeChange: (PageTurnMode) -> Unit,
    onAutoPageEnabledChange: (Boolean) -> Unit,
    onCatalogClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onFontClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(238.dp)
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
            // PageTurnSection - 高度 90dp
            PageTurnSection(
                pageTurnMode = pageTurnMode,
                currentColorScheme = currentColorScheme,
                onPageTurnModeChange = onPageTurnModeChange
            )

            // AutoPageSection - 高度 66dp
            AutoPageSection(
                autoPageEnabled = autoPageEnabled,
                currentColorScheme = currentColorScheme,
                onAutoPageEnabledChange = onAutoPageEnabledChange
            )

            // 底部控制按钮 - 高度 66dp
            BottomControlButtons(
                onCatalogClick = onCatalogClick,
                onBrightnessClick = onBrightnessClick,
                onFontClick = onFontClick,
                onMoreClick = onMoreClick,
                activeButton = ReaderPanel.MORE,
                currentColorScheme = currentColorScheme
            )
        }
    }
}

/**
 * 翻页方式选择区
 *
 * 设计规格：
 * - 高度: 90dp
 * - 内边距: vertical: 12dp, horizontal: 20dp
 * - 间距: 12dp
 */
@Composable
private fun PageTurnSection(
    pageTurnMode: PageTurnMode,
    currentColorScheme: ReaderColorScheme,
    onPageTurnModeChange: (PageTurnMode) -> Unit
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
            text = "翻页方式",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = currentColorScheme.text
        )

        // 分段控制器
        PageTurnSegmentedControl(
            options = listOf("仿真", "覆盖", "滑动"),
            selectedIndex = when (pageTurnMode) {
                PageTurnMode.REAL -> 0
                PageTurnMode.COVER -> 1
                PageTurnMode.SLIDE -> 2
                else -> 2  // 默认滑动
            },
            currentColorScheme = currentColorScheme,
            onOptionSelected = { index ->
                val values = listOf(
                    PageTurnMode.REAL,
                    PageTurnMode.COVER,
                    PageTurnMode.SLIDE
                )
                onPageTurnModeChange(values[index])
            }
        )
    }
}

/**
 * 翻页方式分段控制器
 *
 * 设计规格（来自 Pencil 设计稿 Read-More 页面 pageTurnOptions 2O5S6）：
 * - 容器无固定高度，wrap content
 * - justifyContent: space_between
 * - 选项间距: 12dp（gap）
 * - 每个选项: height 36dp, cornerRadius 8dp, width fill_container
 * - 选中背景: $--accent (Accent)
 * - 选中文字: #FFFFFF, fontWeight 600
 * - 未选中背景: #F5F5F5
 * - 未选中文字: $--foreground-tertiary, fontWeight normal
 */
@Composable
private fun PageTurnSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    currentColorScheme: ReaderColorScheme,
    onOptionSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) {
                            Accent  // 选中状态：使用 Accent 色
                        } else {
                            Color(0xFFF5F5F5)  // 未选中状态：浅灰色
                        }
                    )
                    .clickable { onOptionSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) {
                        Color.White  // 选中状态：白色文字
                    } else {
                        currentColorScheme.text.copy(alpha = 0.6f)  // tertiary
                    }
                )
            }
        }
    }
}

/**
 * 自动翻页选择区
 *
 * 设计规格：
 * - 高度: 66dp
 * - 内边距: vertical: 12dp, horizontal: 20dp
 */
@Composable
private fun AutoPageSection(
    autoPageEnabled: Boolean,
    currentColorScheme: ReaderColorScheme,
    onAutoPageEnabledChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 标题
        Text(
            text = "自动翻页",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = currentColorScheme.text
        )

        // 开关组件
        AutoPageToggle(
            enabled = autoPageEnabled,
            currentColorScheme = currentColorScheme,
            onEnabledChange = onAutoPageEnabledChange
        )
    }
}

/**
 * 自动翻页开关组件
 *
 * 设计规格（来自 Pencil 设计稿 Read-More 页面 autoPageToggle xOADb）：
 * - 轨道宽度: 51dp, 高度: 31dp, 圆角: 16dp, padding: 2dp
 * - 轨道颜色（关闭）: #E5E5EA
 * - 轨道颜色（开启）: Accent (#FF6B4A)
 * - 滑块尺寸: 27×27dp, 圆角: 14dp, 颜色: #FFFFFF
 * - 滑块阴影: blur=2, color=#00000020, offset=(0,1)
 */
@Composable
private fun AutoPageToggle(
    enabled: Boolean,
    currentColorScheme: ReaderColorScheme,
    onEnabledChange: (Boolean) -> Unit
) {
    // 轨道颜色
    val trackColor = if (enabled) {
        Accent
    } else {
        Color(0xFFE5E5EA)  // 统一使用浅灰色
    }

    // 滑块尺寸和内边距
    val thumbSize = 27.dp
    val padding = 2.dp  // 滑块与轨道的间距

    Box(
        modifier = Modifier
            .width(51.dp)
            .height(31.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor)
            .clickable { onEnabledChange(!enabled) }
            .padding(padding),
        contentAlignment = if (enabled) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .clip(RoundedCornerShape(14.dp))  // 设计稿圆角 14dp
                .background(Color.White)
                .drawBehind {
                    // 滑块阴影: blur=2, color=#00000020, offset=(0,1)
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            asFrameworkPaint().apply {
                                isAntiAlias = true
                                color = android.graphics.Color.TRANSPARENT
                                setShadowLayer(
                                    2f,   // blur radius
                                    0f,   // dx
                                    1f,   // dy
                                    android.graphics.Color.argb(32, 0, 0, 0)  // #00000020
                                )
                            }
                        }
                        canvas.drawRoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            radiusX = 14.dp.toPx(),
                            radiusY = 14.dp.toPx(),
                            paint = paint
                        )
                    }
                }
        )
    }
}
