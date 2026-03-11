package com.vonfly.read.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * 阅读器设置
 *
 * 独立于系统 Material Theme，用于管理阅读器的字体大小、行距、背景色等设置。
 * 默认值来自设计稿：fontSize=18sp, lineHeight=1.8
 */
data class ReaderSettings(
    val fontSize: TextUnit = 18.sp,
    val lineHeight: TextUnit = 32.4.sp, // 18 * 1.8 = 32.4
    val letterSpacing: TextUnit = 0.sp,
    val colorScheme: ReaderColorScheme = ReaderColorScheme.Default
)

/**
 * 阅读器配色方案
 */
enum class ReaderColorScheme(val background: Color, val text: Color, val secondary: Color) {
    /** 米白（默认） */
    Default(
        background = Color(0xFFFAF8F5),
        text = Color(0xFF1A1A1A),
        secondary = Color(0xFF666666)
    ),
    /** 夜间 */
    Night(
        background = Color(0xFF1C1C1E),
        text = Color(0xFFE5E5EA),
        secondary = Color(0xFF8E8E93)
    ),
    /** 护眼暖色（米黄） */
    Sepia(
        background = Color(0xFFF4ECD8),
        text = Color(0xFF5B4636),
        secondary = Color(0xFF8B7355)
    ),
    /** 绿色护眼 */
    Green(
        background = Color(0xFFCCE8CC),
        text = Color(0xFF1A3A1A),
        secondary = Color(0xFF4A6A4A)
    ),
    /** 粉色 */
    Pink(
        background = Color(0xFFFFE4E6),
        text = Color(0xFF4A1A1A),
        secondary = Color(0xFF8B5555)
    )
}

/**
 * 阅读器设置 CompositionLocal
 */
val LocalReaderSettings = compositionLocalOf { ReaderSettings() }

/**
 * 阅读器主题
 *
 * 使用方式：
 * ```kotlin
 * ReaderTheme(settings = readerSettings) {
 *     // 阅读器内容
 * }
 * ```
 */
@Composable
fun ReaderTheme(
    settings: ReaderSettings,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalReaderSettings provides settings) {
        content()
    }
}

/**
 * 获取阅读器文本样式
 * 设计稿: fontSize=18, color=#333333, lineHeight=1.8
 */
@Composable
fun readerTextStyle(): TextStyle {
    val settings = LocalReaderSettings.current
    return TextStyle(
        fontSize = settings.fontSize,
        lineHeight = settings.lineHeight,
        letterSpacing = settings.letterSpacing,
        color = Color(0xFF333333), // 设计稿指定颜色
        fontWeight = FontWeight.Normal
    )
}

/**
 * 获取阅读器章节标题样式
 * 设计稿: fontSize=14, fontWeight=500, color=$--foreground-secondary
 */
@Composable
fun readerTitleStyle(): TextStyle {
    return TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Color(0xFF6B6B6B), // ForegroundSecondary
        fontWeight = FontWeight.Medium
    )
}
