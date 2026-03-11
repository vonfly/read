package com.vonfly.read.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vonfly.read.domain.model.ReadingProgress

/**
 * 阅读进度 Room Entity
 */
@Entity(tableName = "reading_progress")
data class ReadingProgressEntity(
    @PrimaryKey
    val bookId: String,
    val pageIndex: Int,
    val positionInPage: Float,
    val lastReadAt: Long
)

/**
 * Entity 转 Domain 模型
 */
fun ReadingProgressEntity.toDomain(): ReadingProgress = ReadingProgress(
    bookId = bookId,
    pageIndex = pageIndex,
    positionInPage = positionInPage,
    lastReadAt = lastReadAt
)

/**
 * Domain 模型转 Entity
 */
fun ReadingProgress.toEntity(): ReadingProgressEntity = ReadingProgressEntity(
    bookId = bookId,
    pageIndex = pageIndex,
    positionInPage = positionInPage,
    lastReadAt = lastReadAt
)
