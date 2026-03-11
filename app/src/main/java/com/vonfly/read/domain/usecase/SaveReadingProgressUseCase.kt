package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.ReadingProgress
import com.vonfly.read.domain.repository.ReadingProgressRepository
import javax.inject.Inject

/**
 * 保存阅读进度 UseCase
 */
class SaveReadingProgressUseCase @Inject constructor(
    private val repository: ReadingProgressRepository
) {
    /**
     * 保存阅读进度到本地
     *
     * @param progress 阅读进度
     * @return 保存结果
     */
    suspend operator fun invoke(progress: ReadingProgress): Result<Unit> =
        repository.saveLocalProgress(progress)
}
