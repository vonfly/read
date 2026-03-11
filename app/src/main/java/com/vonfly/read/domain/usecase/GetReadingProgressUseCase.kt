package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.ReadingProgress
import com.vonfly.read.domain.repository.ReadingProgressRepository
import javax.inject.Inject

/**
 * 获取阅读进度 UseCase
 */
class GetReadingProgressUseCase @Inject constructor(
    private val repository: ReadingProgressRepository
) {
    /**
     * 获取指定书籍的阅读进度
     *
     * @param bookId 书籍 ID
     * @return 阅读进度，如果不存在则返回 null
     */
    suspend operator fun invoke(bookId: String): ReadingProgress? =
        repository.getProgress(bookId)
}
