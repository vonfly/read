package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.Book
import com.vonfly.read.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取书架列表 UseCase
 *
 * 单一职责：返回所有书籍的 Flow，供 UI 层订阅。
 * 遵循离线优先原则，数据来源于 Room。
 */
class GetBookListUseCase @Inject constructor(
    private val repository: BookRepository
) {
    /**
     * 获取书架列表
     *
     * @return 书籍列表的 Flow，按更新时间降序排列
     */
    operator fun invoke(): Flow<List<Book>> = repository.observeAllBooks()
}
