package com.vonfly.read.domain.model

/**
 * 翻页方式枚举
 *
 * 设计规格来自 Pencil 设计稿 Read-More 页面 PageTurnSection：
 * - REAL: 仿真翻页（模拟真实书籍翻页效果）
 * - COVER: 覆盖翻页（新页面从右侧覆盖当前页面）
 * - SLIDE: 滑动翻页（页面水平滑动切换）
 */
enum class PageTurnMode {
    REAL,    // 仿真翻页
    COVER,   // 覆盖翻页
    SLIDE    // 滑动翻页
}
