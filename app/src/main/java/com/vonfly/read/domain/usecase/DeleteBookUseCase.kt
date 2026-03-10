package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.repository.BookRepository
import javax.inject.Inject

/**
 * 删除书籍 UseCase
 *
 * 单一职责：从书架删除指定书籍。
 * 删除操作涉及本地数据库，未来可扩展为同步删除服务端数据。
 */
class DeleteBookUseCase @Inject constructor(
    private val repository: BookRepository
) {
    /**
     * 删除书籍
     *
     * @param id 书籍 ID
     * @return 删除结果，成功返回 Result.success，失败返回 Result.failure
     */
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteBook(id)
}
