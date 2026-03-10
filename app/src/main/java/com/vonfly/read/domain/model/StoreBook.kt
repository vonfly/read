package com.vonfly.read.domain.model

/**
 * 书城书籍领域模型
 *
 * 用于书城热门榜单展示，包含排名、评分、阅读量等信息。
 *
 * @param id 书籍唯一标识（UUID v4 字符串）
 * @param rank 排名（1-5）
 * @param title 书名
 * @param author 作者
 * @param rating 评分（0.0 - 10.0）
 * @param readCount 阅读量描述（如"128万人读过"）
 * @param coverGradientStart 封面渐变起始色，格式 "#667eea"
 * @param coverGradientEnd 封面渐变结束色，格式 "#764ba2"
 */
data class StoreBook(
    val id: String,
    val rank: Int,
    val title: String,
    val author: String,
    val rating: Float,
    val readCount: String,
    val coverGradientStart: String,
    val coverGradientEnd: String
)
