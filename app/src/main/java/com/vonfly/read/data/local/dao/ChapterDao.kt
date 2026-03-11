package com.vonfly.read.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vonfly.read.data.local.entity.ChapterEntity
import kotlinx.coroutines.flow.Flow

/**
 * 章节 DAO
 *
 * 定义章节数据的 Room 操作接口。
 */
@Dao
interface ChapterDao {

    /**
     * 观察指定书籍的章节列表
     *
     * @param bookId 书籍 ID
     * @return 章节实体列表的 Flow，按章节序号升序排列
     */
    @Query("SELECT * FROM chapters WHERE bookId = :bookId ORDER BY `index` ASC")
    fun observeByBookId(bookId: String): Flow<List<ChapterEntity>>

    /**
     * 获取指定书籍的章节列表
     *
     * @param bookId 书籍 ID
     * @return 章节实体列表，按章节序号升序排列
     */
    @Query("SELECT * FROM chapters WHERE bookId = :bookId ORDER BY `index` ASC")
    suspend fun getByBookId(bookId: String): List<ChapterEntity>

    /**
     * 插入或更新章节
     *
     * 若已存在相同 id 的记录则更新，否则插入。
     */
    @Upsert
    suspend fun upsert(entity: ChapterEntity)

    /**
     * 批量插入或更新章节
     *
     * 若已存在相同 id 的记录则更新，否则插入。
     */
    @Upsert
    suspend fun upsertAll(entities: List<ChapterEntity>)

    /**
     * 删除指定书籍的所有章节
     *
     * @param bookId 书籍 ID
     */
    @Query("DELETE FROM chapters WHERE bookId = :bookId")
    suspend fun deleteByBookId(bookId: String)
}