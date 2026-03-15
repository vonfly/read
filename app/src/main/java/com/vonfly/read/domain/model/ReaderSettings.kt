package com.vonfly.read.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * 阅读器设置数据类
 *
 * 存储阅读器的字体大小、行距、颜色主题等设置。
 * 通过 DataStore 持久化到本地。
 */
data class ReaderSettings(
    val fontSize: TextUnit = 18.sp,
    val lineHeight: Float = 1.8f,
    val letterSpacing: Float = 0f,  // 字间距（sp 单位倍数），默认 0
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default,
    val brightness: Float = 1.0f  // 亮度值 0.0-1.0，默认 100%
)

/**
 * 阅读器颜色主题
 *
 * 设计规格来自 Pencil 设计稿 Read-Brightness 页面 BackgroundSection：
 * - 白色: #F9F9F9 (默认)
 * - 米色: #F5F0E1 (护眼米色)
 * - 粉色: #FFE4E8 (粉色护眼)
 * - 绿色: #E8F5E9 (绿色护眼)
 * - 深色: #1A1A1A (夜间模式)
 * - 奶油: #FDF8F0 (奶油色)
 *
 * @param background 内容区域背景色
 * @param text 文本色
 * @param panelBackground 面板背景色
 * @param sliderTrack 滑块轨道颜色
 * @param sliderActive 滑块已读部分颜色
 * @param sliderThumbBorder 滑块边框颜色
 */
enum class ReaderColorScheme(
    val background: Color,
    val text: Color,
    val panelBackground: Color,
    val sliderTrack: Color,
    val sliderActive: Color,
    val sliderThumbBorder: Color
) {
    // 白色 - 默认
    Default(
        background = Color(0xFFF9F9F9),
        text = Color(0xFF333333),
        panelBackground = Color(0xFFF9F9F9),
        sliderTrack = Color(0xFFE5E5EA),
        sliderActive = Color(0xFFC5C5CA),
        sliderThumbBorder = Color(0x25000000)
    ),
    // 米色（护眼）
    Sepia(
        background = Color(0xFFF5F0E1),
        text = Color(0xFF5B4636),
        panelBackground = Color(0xFFF5F0E1),
        sliderTrack = Color(0xFFE5E5EA),
        sliderActive = Color(0xFFC5B8A8),
        sliderThumbBorder = Color(0x25000000)
    ),
    // 粉色（护眼）
    Pink(
        background = Color(0xFFFFE4E8),
        text = Color(0xFF5B4636),
        panelBackground = Color(0xFFFFE4E8),
        sliderTrack = Color(0xFFE5E5EA),
        sliderActive = Color(0xFFE5A5A8),
        sliderThumbBorder = Color(0x25000000)
    ),
    // 绿色（护眼）
    Green(
        background = Color(0xFFE8F5E9),
        text = Color(0xFF1A3A1A),
        panelBackground = Color(0xFFE8F5E9),
        sliderTrack = Color(0xFFE5E5EA),
        sliderActive = Color(0xFFA5C5A5),
        sliderThumbBorder = Color(0x25000000)
    ),
    // 深色（夜间）- 面板背景与内容区域相同
    Night(
        background = Color(0xFF1A1A1A),
        text = Color(0xFFE5E5EA),
        panelBackground = Color(0xFF1A1A1A),  // 与内容区域相同，不透明
        sliderTrack = Color(0xFF3A3A3A),
        sliderActive = Color(0xFF8A8A8A),
        sliderThumbBorder = Color(0x33FFFFFF)
    ),
    // 奶油色
    Cream(
        background = Color(0xFFFDF8F0),
        text = Color(0xFF5B4636),
        panelBackground = Color(0xFFFDF8F0),
        sliderTrack = Color(0xFFE5E5EA),
        sliderActive = Color(0xFFC5B8A8),
        sliderThumbBorder = Color(0x25000000)
    )
}
