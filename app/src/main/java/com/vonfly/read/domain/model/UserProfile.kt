package com.vonfly.read.domain.model

/**
 * 用户资料数据类
 *
 * 本期使用 Mock 数据，后续可从账号系统获取真实数据
 */
data class UserProfile(
    val nickname: String = "阅读爱好者",
    val userId: String = "ID: 12345678"
)
