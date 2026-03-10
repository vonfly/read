package com.vonfly.read.domain.model

/**
 * 书籍领域模型
 *
 * @param id 书籍唯一标识（UUID v4 字符串）
 * @param title 书名
 * @param coverGradientStart 封面渐变起始色，格式 "#667eea"
 * @param coverGradientEnd 封面渐变结束色，格式 "#764ba2"
 * @param lastReadAt 最后阅读时间戳（毫秒），null 表示从未阅读
 * @param readingProgress 阅读进度 0.0 - 1.0
 * @param currentChapter 当前章节名
 * @param createdAt 创建时间戳（毫秒）
 * @param updatedAt 更新时间戳（毫秒）
 */
data class Book(
    val id: String,
    val title: String,
    val coverGradientStart: String,
    val coverGradientEnd: String,
    val lastReadAt: Long?,
    val readingProgress: Float,
    val currentChapter: String?,
    val createdAt: Long,
    val updatedAt: Long
)
