package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.Category
import com.vonfly.read.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取分类列表用例
 *
 * 单一职责：从仓库获取分类数据。
 */
class GetCategoriesUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
}
