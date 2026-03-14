package com.vonfly.read.domain.model

/**
 * 阅读统计数据类
 *
 * 本期使用 Mock 数据，后续可从 Room 查询真实数据
 */
data class ReadingStats(
    val booksRead: Int = 128,
    val readingHours: Int = 256,
    val booksFavorited: Int = 32
)
