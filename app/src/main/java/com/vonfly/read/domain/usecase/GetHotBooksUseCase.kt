package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.StoreBook
import com.vonfly.read.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取热门书籍用例
 *
 * 单一职责：从仓库获取热门书籍数据，支持按分类筛选。
 */
class GetHotBooksUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    /**
     * 获取热门书籍
     *
     * @param categoryId 分类 ID，null 表示全部/推荐
     * @return 热门书籍列表的 Flow
     */
    operator fun invoke(categoryId: String?): Flow<List<StoreBook>> =
        repository.getHotBooks(categoryId)
}
