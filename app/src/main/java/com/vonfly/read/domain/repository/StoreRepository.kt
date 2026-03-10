package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.Banner
import com.vonfly.read.domain.model.Category
import com.vonfly.read.domain.model.StoreBook
import kotlinx.coroutines.flow.Flow

/**
 * 书城仓库接口
 *
 * 定义书城数据的访问契约，实现类负责从数据源（Mock/网络）获取数据。
 */
interface StoreRepository {

    /**
     * 获取 Banner 列表
     *
     * @return Banner 列表的 Flow
     */
    fun getBanners(): Flow<List<Banner>>

    /**
     * 获取分类列表
     *
     * @return 分类列表的 Flow
     */
    fun getCategories(): Flow<List<Category>>

    /**
     * 获取热门书籍
     *
     * @param categoryId 分类 ID，null 表示全部/推荐
     * @return 热门书籍列表的 Flow
     */
    fun getHotBooks(categoryId: String?): Flow<List<StoreBook>>
}
