package com.vonfly.read.data.local.preferences

import com.vonfly.read.domain.model.ReadingStats
import com.vonfly.read.domain.model.UserProfile
import com.vonfly.read.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户偏好 Repository 实现
 *
 * 本期使用 Mock 数据，后续可从 Room 查询真实数据
 */
@Singleton
class UserPreferencesRepositoryImpl @Inject constructor() : UserPreferencesRepository {

    override fun observeUserProfile(): Flow<UserProfile> = flow {
        // Mock 数据，后续可从账号系统获取
        emit(UserProfile(
            nickname = "阅读爱好者",
            userId = "ID: 12345678"
        ))
    }

    override fun observeReadingStats(): Flow<ReadingStats> = flow {
        // Mock 数据，后续可从 Room 查询
        // booksRead: 统计 Book 表 status = READ 的记录数
        // readingHours: 累计 ReadingSession.duration（分钟转小时）
        // booksFavorited: 统计 Book 表 isFavorite = true 的记录数
        emit(ReadingStats(
            booksRead = 128,
            readingHours = 256,
            booksFavorited = 32
        ))
    }
}
