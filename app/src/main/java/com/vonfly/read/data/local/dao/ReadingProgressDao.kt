package com.vonfly.read.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vonfly.read.data.local.entity.ReadingProgressEntity
import kotlinx.coroutines.flow.Flow

/**
 * 阅读进度 DAO
 */
@Dao
interface ReadingProgressDao {

    /**
     * 观察指定书籍的阅读进度
     */
    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId")
    fun observeProgress(bookId: String): Flow<ReadingProgressEntity?>

    /**
     * 获取指定书籍的阅读进度（一次性读取）
     */
    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId")
    suspend fun getProgress(bookId: String): ReadingProgressEntity?

    /**
     * 插入或更新阅读进度
     */
    @Upsert
    suspend fun upsert(progress: ReadingProgressEntity)
}
