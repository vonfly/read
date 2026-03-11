package com.vonfly.read.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 书籍详情 Room 实体
 *
 * 存储书籍的完整信息，用于书籍详情页展示。
 * 与 BookEntity 分离，BookEntity 用于书架列表，BookDetailEntity 用于详情页。
 *
 * @param id 书籍唯一标识（UUID v4 字符串）
 * @param title 书名
 * @param author 作者名
 * @param coverGradientStart 封面渐变起始色，格式 "#667eea"
 * @param coverGradientEnd 封面渐变结束色，格式 "#764ba2"
 * @param rating 评分（0.0 - 5.0）
 * @param readerCount 阅读人数
 * @param description 书籍简介
 * @param totalChapters 总章节数
 * @param isInShelf 是否已在书架中
 * @param updatedAt 更新时间戳（毫秒）
 */
@Entity(tableName = "book_details")
data class BookDetailEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val coverGradientStart: String,
    val coverGradientEnd: String,
    val rating: Float,
    val readerCount: Int,
    val description: String,
    val totalChapters: Int,
    val isInShelf: Boolean,
    val updatedAt: Long
)
