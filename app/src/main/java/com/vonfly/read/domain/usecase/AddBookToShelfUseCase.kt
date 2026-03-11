package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.repository.BookDetailRepository
import javax.inject.Inject

/**
 * 将书籍加入书架 UseCase
 *
 * 单一职责：将指定书籍添加到用户的书架中。
 */
class AddBookToShelfUseCase @Inject constructor(
    private val repository: BookDetailRepository
) {
    /**
     * 将书籍加入书架
     *
     * @param bookId 书籍 ID
     * @return 添加结果，成功返回 Result.success，失败返回 Result.failure
     */
    suspend operator fun invoke(bookId: String): Result<Unit> =
        repository.addToShelf(bookId)
}
