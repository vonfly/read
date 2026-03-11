package com.vonfly.read.domain.model

/**
 * 阅读进度模型
 *
 * 记录用户在某本书中的阅读位置，用于本地持久化和恢复阅读位置。
 */
data class ReadingProgress(
    val bookId: String,
    val pageIndex: Int,
    val positionInPage: Float = 0f,
    val lastReadAt: Long = System.currentTimeMillis()
)
