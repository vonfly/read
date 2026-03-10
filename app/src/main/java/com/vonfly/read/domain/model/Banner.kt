package com.vonfly.read.domain.model

/**
 * Banner 领域模型
 *
 * 用于书城活动轮播展示。
 *
 * @param id Banner 唯一标识
 * @param title 活动标题（如"限时免费"）
 * @param description 活动描述（如"精选好书限时免费阅读"）
 * @param buttonText 按钮文字（如"立即查看"）
 * @param gradientStart 渐变起始色，格式 "#667eea"
 * @param gradientEnd 渐变结束色，格式 "#764ba2"
 */
data class Banner(
    val id: String,
    val title: String,
    val description: String,
    val buttonText: String,
    val gradientStart: String,
    val gradientEnd: String
)
