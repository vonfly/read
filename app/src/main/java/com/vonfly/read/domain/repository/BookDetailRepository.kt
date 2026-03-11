package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.BookDetail

/**
 * 书籍详情仓库接口
 *
 * 定义书籍详情数据的访问契约，实现类负责从数据源（Room/网络）获取数据。
 * 遵循离线优先原则：网络数据必须先写入 Room 再通知 UI。
 */
interface BookDetailRepository {

    /**
     * 获取书籍详情
     *
     * @param bookId 书籍 ID
     * @return 书籍详情，成功返回 Result.success，失败返回 Result.failure
     */
    suspend fun getBookDetail(bookId: String): Result<BookDetail>

    /**
     * 将书籍添加到书架
     *
     * @param bookId 书籍 ID
     * @return 添加结果，成功返回 Result.success，失败返回 Result.failure
     */
    suspend fun addToShelf(bookId: String): Result<Unit>
}
