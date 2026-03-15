package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.ui.theme.Accent

/**
 * 自动翻页配置面板（滚动模式专用）
 *
 * 设计规格：
 * - 面板高度: 150dp
 * - 顶部圆角: 16dp
 * - 背景色: currentColorScheme.panelBackground
 * - 阴影: blur=20, color=#00000025
 *
 * @param autoPageEnabled 是否启用自动翻页（始终为 true）
 * @param autoPageSpeed 自动翻页速度倍数（1.0-3.0）
 * @param autoPageInterval 自动翻页间隔秒数（1-10）- 滚动模式不使用
 * @param currentColorScheme 当前颜色主题
 * @param onAutoPageEnabledChange 自动翻页开关变化回调（关闭时触发）
 * @param onAutoPageSpeedChange 速度变化回调
 * @param onAutoPageIntervalChange 间隔变化回调 - 滚动模式不使用
 * @param onCatalogClick 目录按钮点击回调 - 滚动模式不使用
 * @param onBrightnessClick 亮度按钮点击回调 - 滚动模式不使用
 * @param onFontClick 字体按钮点击回调 - 滚动模式不使用
 * @param onMoreClick 更多按钮点击回调 - 滚动模式用于恢复滚动
 * @param modifier Modifier
 */
@Composable
fun AutoPageConfigPanel(
    autoPageEnabled: Boolean,
    autoPageSpeed: Float,
    autoPageInterval: Int,
    currentColorScheme: ReaderColorScheme,
    onAutoPageEnabledChange: (Boolean) -> Unit,
    onAutoPageSpeedChange: (Float) -> Unit,
    onAutoPageIntervalChange: (Int) -> Unit,
    onCatalogClick: () -> Unit,
    onBrightnessClick: () -> Unit,
    onFontClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = currentColorScheme == ReaderColorScheme.Night

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
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
            // 消费面板区域的点击事件，阻止传播到底层遮罩
            .pointerInput(Unit) {
                detectTapGestures {
                    // 不做任何处理，仅消费事件
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            // 速度设置区域 - 高度 50dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "速度",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = currentColorScheme.text
                )

                // 速度分段控制器：1x, 1.5x, 2x, 3x
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val speeds = listOf(1.0f, 1.5f, 2.0f, 3.0f)
                    speeds.forEach { speedValue ->
                        val isSelected = autoPageSpeed == speedValue

                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isSelected) Accent
                                    else if (isDarkTheme) Color(0xFF3A3A3A)
                                    else Color(0xFFF5F5F5)
                                )
                                .clickable { onAutoPageSpeedChange(speedValue) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (speedValue == 1.0f) "1x" else "${speedValue}x",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else currentColorScheme.text.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // 底部退出按钮区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Accent)
                    .clickable { onAutoPageEnabledChange(false) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "退出自动翻页",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
