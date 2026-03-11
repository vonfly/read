package com.vonfly.read.domain.model

/**
 * 分页内容模型
 *
 * 表示阅读器中的一页内容，包含多个段落和可选的章节标题。
 */
data class PageContent(
    val pageIndex: Int,
    val paragraphs: List<String>,
    val chapterTitle: String? = null
)
