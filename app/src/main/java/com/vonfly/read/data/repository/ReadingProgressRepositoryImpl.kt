package com.vonfly.read.data.repository

import com.vonfly.read.data.local.dao.ReadingProgressDao
import com.vonfly.read.data.local.entity.toDomain
import com.vonfly.read.data.local.entity.toEntity
import com.vonfly.read.domain.model.ReadingProgress
import com.vonfly.read.domain.repository.ReadingProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 阅读进度仓库实现
 *
 * 本期仅实现本地存储，syncProgressToRemote 返回成功（预留接口）。
 */
class ReadingProgressRepositoryImpl @Inject constructor(
    private val dao: ReadingProgressDao
) : ReadingProgressRepository {

    override fun observeProgress(bookId: String): Flow<ReadingProgress?> =
        dao.observeProgress(bookId).map { it?.toDomain() }

    override suspend fun getProgress(bookId: String): ReadingProgress? =
        dao.getProgress(bookId)?.toDomain()

    override suspend fun saveLocalProgress(progress: ReadingProgress): Result<Unit> = try {
        dao.upsert(progress.toEntity())
        Result.success(Unit)
    } catch (e: Exception) {
        if (e is kotlinx.coroutines.CancellationException) throw e
        Result.failure(e)
    }

    override suspend fun syncProgressToRemote(bookId: String): Result<Unit> {
        // 本期无服务端同步，预留接口
        return Result.success(Unit)
    }
}
