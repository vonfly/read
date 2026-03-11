package com.vonfly.read.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 章节 Room 实体
 *
 * 存储书籍的章节信息，与 BookDetailEntity 通过 bookId 关联。
 *
 * @param id 章节唯一标识
 * @param bookId 所属书籍 ID
 * @param title 章节标题
 * @param index 章节序号（从 0 开始）
 * @param isFree 是否免费章节
 */
@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val title: String,
    val index: Int,
    val isFree: Boolean
)
