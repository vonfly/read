package com.vonfly.read.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

/**
 * 书签数据模型
 *
 * @param id 书签唯一标识
 * @param bookId 所属书籍 ID
 * @param title 书籍标题
 * @param author 作者
 * @param chapter 章节号
 * @param preview 书签位置的内容预览
 * @param coverGradient 封面渐变颜色（模拟封面）
 * @param createdAt 创建时间戳
 */
@Serializable
data class Bookmark(
    val id: String,
    val bookId: String,
    val title: String,
    val author: String,
    val chapter: Int,
    val preview: String,
    val coverGradientColors: List<Long>,  // 使用 Long 存储 ARGB 颜色值，便于序列化
    val createdAt: Long
) {
    /**
     * 获取封面渐变色的 Compose Color 列表
     */
    fun getCoverGradient(): List<Color> = coverGradientColors.map { Color(it) }
}
