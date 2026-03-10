package com.vonfly.read.data.mapper

import com.vonfly.read.data.local.entity.BookEntity
import com.vonfly.read.domain.model.Book

/**
 * BookEntity 转换为 Book 领域模型
 */
fun BookEntity.toDomain(): Book = Book(
    id = id,
    title = title,
    coverGradientStart = coverGradientStart,
    coverGradientEnd = coverGradientEnd,
    lastReadAt = lastReadAt,
    readingProgress = readingProgress,
    currentChapter = currentChapter,
    createdAt = createdAt,
    updatedAt = updatedAt
)

/**
 * Book 领域模型转换为 BookEntity
 */
fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    coverGradientStart = coverGradientStart,
    coverGradientEnd = coverGradientEnd,
    lastReadAt = lastReadAt,
    readingProgress = readingProgress,
    currentChapter = currentChapter,
    createdAt = createdAt,
    updatedAt = updatedAt
)
