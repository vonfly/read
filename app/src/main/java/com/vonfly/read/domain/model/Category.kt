package com.vonfly.read.domain.model

/**
 * 分类领域模型
 *
 * 用于书城分类标签展示。
 *
 * @param id 分类唯一标识
 * @param name 分类名称（如"推荐"、"小说"、"文学"等）
 */
data class Category(
    val id: String,
    val name: String
)
