package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.UserProfile
import com.vonfly.read.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 获取用户资料 UseCase
 *
 * 本期返回 Mock 数据
 */
class GetUserProfileUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserProfile> = repository.observeUserProfile()
}
