package com.vonfly.read.data.repository

import com.vonfly.read.domain.model.BookDetail
import com.vonfly.read.domain.model.Chapter
import com.vonfly.read.domain.repository.BookDetailRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * 书籍详情仓库实现
 *
 * 本期使用 Mock 数据，模拟书籍详情数据（三体）。
 * 后续接入真实数据源时，替换为从 Room/网络获取数据。
 */
class BookDetailRepositoryImpl @Inject constructor(
    // TODO: 后续注入 BookDetailDao 和 ChapterDao
    // private val bookDetailDao: BookDetailDao,
    // private val chapterDao: ChapterDao
) : BookDetailRepository {

    /**
     * 获取书籍详情
     *
     * 本期返回 Mock 数据，模拟《三体》的书籍详情。
     *
     * @param bookId 书籍 ID
     * @return 书籍详情，成功返回 Result.success，失败返回 Result.failure
     */
    override suspend fun getBookDetail(bookId: String): Result<BookDetail> = try {
        // 模拟网络延迟
        delay(300)

        // Mock 数据：《三体》
        val mockBookDetail = createMockBookDetail(bookId)
        Result.success(mockBookDetail)
    } catch (e: Exception) {
        if (e is kotlinx.coroutines.CancellationException) throw e
        Result.failure(e)
    }

    /**
     * 将书籍添加到书架
     *
     * 本期仅返回成功，不实际操作数据库。
     *
     * @param bookId 书籍 ID
     * @return 添加结果，成功返回 Result.success，失败返回 Result.failure
     */
    override suspend fun addToShelf(bookId: String): Result<Unit> = try {
        // 模拟网络延迟
        delay(200)

        // TODO: 后续实现真实逻辑
        // 1. 检查书籍是否已在书架
        // 2. 若不在，则添加到 books 表
        // 3. 更新 book_details 表的 isInShelf 字段

        Result.success(Unit)
    } catch (e: Exception) {
        if (e is kotlinx.coroutines.CancellationException) throw e
        Result.failure(e)
    }

    /**
     * 创建 Mock 书籍详情数据
     */
    private fun createMockBookDetail(bookId: String): BookDetail {
        val chapters = persistentListOf(
            Chapter(id = "$bookId-chapter-0", bookId = bookId, title = "第1章 科学边界", index = 0, isFree = true),
            Chapter(id = "$bookId-chapter-1", bookId = bookId, title = "第2章 三体", index = 1, isFree = true),
            Chapter(id = "$bookId-chapter-2", bookId = bookId, title = "第3章 红岸基地", index = 2, isFree = true),
            Chapter(id = "$bookId-chapter-3", bookId = bookId, title = "第4章 三体问题", index = 3, isFree = false),
            Chapter(id = "$bookId-chapter-4", bookId = bookId, title = "第5章 三体运动", index = 4, isFree = false),
            Chapter(id = "$bookId-chapter-5", bookId = bookId, title = "第6章 宇宙闪烁", index = 5, isFree = false),
            Chapter(id = "$bookId-chapter-6", bookId = bookId, title = "第7章 智子", index = 6, isFree = false),
            Chapter(id = "$bookId-chapter-7", bookId = bookId, title = "第8章 人类的落日", index = 7, isFree = false)
        )

        return BookDetail(
            id = bookId,
            title = "三体",
            author = "刘慈欣",
            coverGradientStart = "#667eea",
            coverGradientEnd = "#764ba2",
            rating = 9.4f,
            readerCount = 1256800,
            description = "文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划\"红岸工程\"取得了突破性进展。但在按下发射键的那一刻，历经劫难且对人类完全失去信心的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……",
            totalChapters = 256,
            isInShelf = false,
            chapters = chapters
        )
    }
}
