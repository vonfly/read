package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

/**
 * 书签仓库接口
 */
interface BookmarkRepository {
    /**
     * 观察所有书签
     *
     * @return 书签列表的 Flow
     */
    fun observeAll(): Flow<List<Bookmark>>
}
