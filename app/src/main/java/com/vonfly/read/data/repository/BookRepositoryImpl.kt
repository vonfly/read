package com.vonfly.read.data.repository

import com.vonfly.read.data.local.dao.BookDao
import com.vonfly.read.data.mapper.toDomain
import com.vonfly.read.domain.model.Book
import com.vonfly.read.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 书籍仓库实现
 *
 * 实现离线优先原则：UI 只观察 Room 的 Flow，所有数据从 Room 读取。
 */
class BookRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : BookRepository {

    /**
     * 观察所有书籍列表
     *
     * 从 Room 读取书籍实体，转换为领域模型后通过 Flow 发射。
     */
    override fun observeAllBooks(): Flow<List<Book>> =
        bookDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * 观察最近阅读的书籍
     *
     * 从 Room 读取最近阅读的书籍实体，转换为领域模型后通过 Flow 发射。
     */
    override fun observeRecentBook(): Flow<Book?> =
        bookDao.observeRecent().map { entity ->
            entity?.toDomain()
        }

    /**
     * 删除书籍
     *
     * @param id 书籍 ID
     * @return 删除结果，成功返回 Result.success，书籍不存在或删除失败返回 Result.failure
     */
    override suspend fun deleteBook(id: String): Result<Unit> = try {
        val entity = bookDao.getById(id)
        if (entity != null) {
            bookDao.delete(entity)
            Result.success(Unit)
        } else {
            Result.failure(NoSuchElementException("Book not found: $id"))
        }
    } catch (e: Exception) {
        if (e is kotlinx.coroutines.CancellationException) throw e
        Result.failure(e)
    }
}
