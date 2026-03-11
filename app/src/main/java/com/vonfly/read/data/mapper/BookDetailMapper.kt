package com.vonfly.read.data.mapper

import com.vonfly.read.data.local.entity.BookDetailEntity
import com.vonfly.read.data.local.entity.ChapterEntity
import com.vonfly.read.domain.model.BookDetail
import com.vonfly.read.domain.model.Chapter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * BookDetailEntity 转换为 BookDetail 领域模型
 *
 * @param chapters 该书籍的章节列表，默认为空列表
 */
fun BookDetailEntity.toDomain(chapters: ImmutableList<Chapter> = emptyList<Chapter>().toImmutableList()): BookDetail =
    BookDetail(
        id = id,
        title = title,
        author = author,
        coverGradientStart = coverGradientStart,
        coverGradientEnd = coverGradientEnd,
        rating = rating,
        readerCount = readerCount,
        description = description,
        totalChapters = totalChapters,
        isInShelf = isInShelf,
        chapters = chapters
    )

/**
 * BookDetail 领域模型转换为 BookDetailEntity
 */
fun BookDetail.toEntity(): BookDetailEntity = BookDetailEntity(
    id = id,
    title = title,
    author = author,
    coverGradientStart = coverGradientStart,
    coverGradientEnd = coverGradientEnd,
    rating = rating,
    readerCount = readerCount,
    description = description,
    totalChapters = totalChapters,
    isInShelf = isInShelf,
    updatedAt = System.currentTimeMillis()
)

/**
 * ChapterEntity 转换为 Chapter 领域模型
 */
fun ChapterEntity.toDomain(): Chapter = Chapter(
    id = id,
    bookId = bookId,
    title = title,
    index = index,
    isFree = isFree
)

/**
 * Chapter 领域模型转换为 ChapterEntity
 */
fun Chapter.toEntity(): ChapterEntity = ChapterEntity(
    id = id,
    bookId = bookId,
    title = title,
    index = index,
    isFree = isFree
)

/**
 * ChapterEntity 列表转换为 Chapter 列表
 */
fun List<ChapterEntity>.toChapterDomainList(): ImmutableList<Chapter> =
    map { it.toDomain() }.toImmutableList()
