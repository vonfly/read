package com.vonfly.read.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vonfly.read.data.local.entity.BookDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * 书籍详情 DAO
 *
 * 定义书籍详情数据的 Room 操作接口。
 */
@Dao
interface BookDetailDao {

    /**
     * 观察 books 表中的书籍详情
     *
     * 由于 BookDetailEntity 和 BookEntity 是不同的表，这里观察 books 表
     * 并在 Repository 层组合数据。
     *
     * @param id 书籍 ID
     * @return 书籍详情实体的 Flow
     */
    @Query("SELECT * FROM book_details WHERE id = :id")
    fun observeById(id: String): Flow<BookDetailEntity?>

    /**
     * 根据 ID 获取书籍详情
     *
     * @param id 书籍 ID
     * @return 书籍详情实体，若不存在则返回 null
     */
    @Query("SELECT * FROM book_details WHERE id = :id")
    suspend fun getById(id: String): BookDetailEntity?

    /**
     * 插入或更新书籍详情
     *
     * 若已存在相同 id 的记录则更新，否则插入。
     */
    @Upsert
    suspend fun upsert(entity: BookDetailEntity)

    /**
     * 更新书架状态
     *
     * @param id 书籍 ID
     * @param isInShelf 是否在书架中
     */
    @Query("UPDATE book_details SET isInShelf = :isInShelf WHERE id = :id")
    suspend fun updateShelfStatus(id: String, isInShelf: Boolean)
}
