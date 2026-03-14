package com.vonfly.read.domain.repository

import com.vonfly.read.domain.model.ReadingStats
import com.vonfly.read.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * 用户偏好 Repository 接口
 *
 * 负责用户资料和阅读统计数据的读取
 * 本期使用 Mock 数据
 */
interface UserPreferencesRepository {
    /**
     * 观察用户资料
     * @return Flow<UserProfile> 用户资料
     */
    fun observeUserProfile(): Flow<UserProfile>

    /**
     * 观察阅读统计
     * @return Flow<ReadingStats> 阅读统计数据
     */
    fun observeReadingStats(): Flow<ReadingStats>
}
