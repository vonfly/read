package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.ReadingProgress
import kotlinx.coroutines.flow.Flow

/**
 * 阅读进度仓库接口
 *
 * 负责管理阅读进度的读写，包括本地存储和未来的云端同步。
 */
interface ReadingProgressRepository {

    /**
     * 观察指定书籍的阅读进度
     *
     * @param bookId 书籍 ID
     * @return 阅读进度 Flow，如果不存在则返回 null
     */
    fun observeProgress(bookId: String): Flow<ReadingProgress?>

    /**
     * 获取指定书籍的阅读进度（一次性读取）
     *
     * @param bookId 书籍 ID
     * @return 阅读进度，如果不存在则返回 null
     */
    suspend fun getProgress(bookId: String): ReadingProgress?

    /**
     * 保存阅读进度到本地
     *
     * @param progress 阅读进度
     * @return 保存结果
     */
    suspend fun saveLocalProgress(progress: ReadingProgress): Result<Unit>

    /**
     * 同步阅读进度到远程服务端
     *
     * @param bookId 书籍 ID
     * @return 同步结果
     */
    suspend fun syncProgressToRemote(bookId: String): Result<Unit>
}
