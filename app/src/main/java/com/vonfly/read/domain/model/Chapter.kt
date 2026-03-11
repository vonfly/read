package com.vonfly.read.domain.model

/**
 * 章节领域模型
 *
 * @param id 章节唯一标识
 * @param bookId 所属书籍 ID
 * @param title 章节标题
 * @param index 章节序号（从 0 开始）
 * @param isFree 是否免费章节
 */
data class Chapter(
    val id: String,
    val bookId: String,
    val title: String,
    val index: Int,
    val isFree: Boolean
)
