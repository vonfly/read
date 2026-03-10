package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.Book
import com.vonfly.read.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取最近阅读的书籍 UseCase
 *
 * 单一职责：返回最近阅读书籍的 Flow，用于书架首页"继续阅读"功能。
 */
class GetRecentBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    /**
     * 获取最近阅读的书籍
     *
     * @return 最近阅读的书籍 Flow，若无阅读记录则返回 null
     */
    operator fun invoke(): Flow<Book?> = repository.observeRecentBook()
}
