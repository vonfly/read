package com.vonfly.read.domain.model

import kotlinx.collections.immutable.ImmutableList

/**
 * 书籍详情领域模型
 *
 * 包含书籍的完整信息，用于书籍详情页展示。
 *
 * @param id 书籍唯一标识
 * @param title 书名
 * @param author 作者名
 * @param coverGradientStart 封面渐变起始色，格式 "#667eea"
 * @param coverGradientEnd 封面渐变结束色，格式 "#764ba2"
 * @param rating 评分（0.0 - 5.0）
 * @param readerCount 阅读人数
 * @param description 书籍简介
 * @param totalChapters 总章节数
 * @param isInShelf 是否已在书架中
 * @param chapters 章节列表
 */
data class BookDetail(
    val id: String,
    val title: String,
    val author: String,
    val coverGradientStart: String,
    val coverGradientEnd: String,
    val rating: Float,
    val readerCount: Int,
    val description: String,
    val totalChapters: Int,
    val isInShelf: Boolean,
    val chapters: ImmutableList<Chapter>
)
