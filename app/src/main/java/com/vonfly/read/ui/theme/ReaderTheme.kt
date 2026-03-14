package com.vonfly.read.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.ReaderColorScheme
import com.vonfly.read.domain.model.ReaderSettings

/**
 * 阅读器设置 CompositionLocal
 *
 * 通过 CompositionLocal 传递阅读器设置到子组件。
 * 默认值使用 ReaderSettings 的默认配置。
 */
val LocalReaderSettings = compositionLocalOf { ReaderSettings() }

/**
 * 阅读器主题包装器
 *
 * 提供阅读器相关的 CompositionLocal 值，包括字体大小、行距、颜色主题等。
 *
 * @param settings 阅读器设置
 * @param content 子组件内容
 */
@Composable
fun ReaderTheme(
    settings: ReaderSettings,
    content: @Composable () -> Unit
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalReaderSettings provides settings
    ) {
        content()
    }
}

/**
 * 计算段落行高
 *
 * 设计稿行高 1.8 对应 Compose 的 lineHeight = fontSize * 1.3f
 * (Compose 内部已乘 fontSize，所以用 1.3f 而非 1.8f)
 *
 * @param fontSize 字体大小
 * @param lineHeightMultiplier 行高倍数（默认 1.8f）
 * @return 计算后的行高
 */
fun calculateLineHeight(
    fontSize: TextUnit,
    lineHeightMultiplier: Float = 1.8f
): TextUnit {
    // Compose 的 lineHeight 是绝对值，设计稿的 1.8 是倍数
    // 实际效果：lineHeight = fontSize * 1.3 (因为 Compose 内部有额外计算)
    return fontSize * (lineHeightMultiplier * 0.72f) // 经验值：1.8 * 0.72 ≈ 1.3
}
