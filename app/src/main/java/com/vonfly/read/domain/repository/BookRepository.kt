package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.Book
import kotlinx.coroutines.flow.Flow

/**
 * 书籍仓库接口
 *
 * 定义书籍数据的访问契约，实现类负责从数据源（Room/网络）获取数据。
 * 遵循离线优先原则：UI 只观察 Room 的 Flow，网络数据必须先写入 Room。
 */
interface BookRepository {

    /**
     * 观察所有书籍列表
     *
     * @return 书籍列表的 Flow，按 updatedAt 降序排列
     */
    fun observeAllBooks(): Flow<List<Book>>

    /**
     * 观察最近阅读的书籍
     *
     * @return 最近阅读的书籍 Flow，若无阅读记录则返回 null
     */
    fun observeRecentBook(): Flow<Book?>

    /**
     * 删除书籍
     *
     * @param id 书籍 ID
     * @return 删除结果，成功返回 Result.success，失败返回 Result.failure
     */
    suspend fun deleteBook(id: String): Result<Unit>
}
