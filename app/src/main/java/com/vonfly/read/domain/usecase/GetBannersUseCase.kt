package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.Banner
import com.vonfly.read.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取 Banner 列表用例
 *
 * 单一职责：从仓库获取 Banner 数据。
 */
class GetBannersUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    operator fun invoke(): Flow<List<Banner>> = repository.getBanners()
}
