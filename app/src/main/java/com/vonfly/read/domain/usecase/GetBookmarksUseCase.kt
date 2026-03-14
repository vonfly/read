package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.Bookmark
import com.vonfly.read.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取书签列表用例
 */
class GetBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    /**
     * 获取所有书签
     *
     * @return 书签列表的 Flow
     */
    operator fun invoke(): Flow<List<Bookmark>> = bookmarkRepository.observeAll()
}
