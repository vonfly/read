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
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default
)

/**
 * 阅读器颜色主题
 *
 * @param background 背景色
 * @param text 文本色
 */
enum class ReaderColorScheme(val background: Color, val text: Color) {
    Default(Color(0xFFF9F9F7), Color(0xFF333333)),
    Night(Color(0xFF1C1C1E), Color(0xFFE5E5EA)),
    Sepia(Color(0xFFF4ECD8), Color(0xFF5B4636)),
    Green(Color(0xFFCCE8CC), Color(0xFF1A3A1A))
}
