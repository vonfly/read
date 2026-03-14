package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.ReadingStats
import com.vonfly.read.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取阅读统计 UseCase
 *
 * 本期返回 Mock 数据，后续从 Room 查询
 */
class GetReadingStatsUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<ReadingStats> = repository.observeReadingStats()
}
