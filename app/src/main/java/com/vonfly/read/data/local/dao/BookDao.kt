package com.vonfly.read.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.vonfly.read.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

/**
 * 书籍 DAO
 *
 * 定义书籍数据的 Room 操作接口。
 * 所有查询方法返回 Flow，支持响应式更新，符合离线优先原则。
 */
@Dao
interface BookDao {

    /**
     * 观察所有书籍列表
     *
     * @return 书籍实体的 Flow，按 updatedAt 降序排列
     */
    @Query("SELECT * FROM books ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<BookEntity>>

    /**
     * 观察最近阅读的书籍
     *
     * @return 最近阅读的书籍实体 Flow，若无阅读记录则返回 null
     */
    @Query("SELECT * FROM books WHERE lastReadAt IS NOT NULL ORDER BY lastReadAt DESC LIMIT 1")
    fun observeRecent(): Flow<BookEntity?>

    /**
     * 插入或更新书籍
     *
     * 若已存在相同 id 的记录则更新，否则插入。
     */
    @Upsert
    suspend fun upsert(entity: BookEntity)

    /**
     * 删除书籍
     */
    @Delete
    suspend fun delete(entity: BookEntity)

    /**
     * 根据 ID 获取书籍
     *
     * @param id 书籍 ID
     * @return 书籍实体，若不存在则返回 null
     */
    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getById(id: String): BookEntity?
}
