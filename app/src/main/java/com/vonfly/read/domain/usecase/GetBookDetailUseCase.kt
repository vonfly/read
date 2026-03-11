package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.BookDetail
import com.vonfly.read.domain.repository.BookDetailRepository
import javax.inject.Inject

/**
 * 获取书籍详情 UseCase
 *
 * 单一职责：根据 bookId 获取书籍详情。
 * 遵循离线优先原则，数据来源于 Room。
 */
class GetBookDetailUseCase @Inject constructor(
    private val repository: BookDetailRepository
) {
    /**
     * 获取书籍详情
     *
     * @param bookId 书籍 ID
     * @return 书籍详情，成功返回 Result.success，失败返回 Result.failure
     */
    suspend operator fun invoke(bookId: String): Result<BookDetail> =
        repository.getBookDetail(bookId)
}
