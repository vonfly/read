package com.vonfly.read.data.repository

import com.vonfly.read.domain.model.Bookmark
import com.vonfly.read.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 书签仓库实现（Mock 数据）
 *
 * 本期使用 Mock 数据，后续替换为 Room 实现。
 */
@Singleton
class BookmarkRepositoryImpl @Inject constructor() : BookmarkRepository {

    override fun observeAll(): Flow<List<Bookmark>> = flow {
        emit(
            listOf(
                Bookmark(
                    id = "bookmark_1",
                    bookId = "book_1",
                    title = "三体",
                    author = "刘慈欣",
                    chapter = 12,
                    preview = "汪淼看着那个幽灵倒计时，时间在不停地闪烁，数字在以秒为单位减少。",
                    coverGradientColors = listOf(0xFF11998e, 0xFF38ef7d),
                    createdAt = System.currentTimeMillis() - 86400000  // 1 天前
                ),
                Bookmark(
                    id = "bookmark_2",
                    bookId = "book_2",
                    title = "百年孤独",
                    author = "加西亚·马尔克斯",
                    chapter = 5,
                    preview = "多年以后，面对行刑队，奥雷里亚诺·布恩迪亚上校将会回想起父亲带他去见识冰块的那个遥远的下午。",
                    coverGradientColors = listOf(0xFF667eea, 0xFF764ba2),
                    createdAt = System.currentTimeMillis() - 172800000  // 2 天前
                ),
                Bookmark(
                    id = "bookmark_3",
                    bookId = "book_3",
                    title = "活着",
                    author = "余华",
                    chapter = 8,
                    preview = "人是为活着本身而活着的，而不是为了活着之外的任何事物所活着。",
                    coverGradientColors = listOf(0xFFf093fb, 0xFFf5576c),
                    createdAt = System.currentTimeMillis() - 259200000  // 3 天前
                )
            )
        )
    }
}
