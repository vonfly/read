package com.vonfly.read.ui.screen.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.ReadingStats
import com.vonfly.read.domain.model.UserProfile
import com.vonfly.read.domain.usecase.GetReadingStatsUseCase
import com.vonfly.read.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class ProfileUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile = UserProfile(),
    val readingStats: ReadingStats = ReadingStats()
)

sealed class ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent()
    data object NavigateToBookmarks : ProfileUiEvent()
    data object NavigateToHistory : ProfileUiEvent()
    data object NavigateToDownloads : ProfileUiEvent()
    data object NavigateToSettings : ProfileUiEvent()
    data object NavigateToProfileEdit : ProfileUiEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getUserProfileUseCase: GetUserProfileUseCase,
    getReadingStatsUseCase: GetReadingStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _event = Channel<ProfileUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        loadData(getUserProfileUseCase, getReadingStatsUseCase)
    }

    private fun loadData(
        getUserProfileUseCase: GetUserProfileUseCase,
        getReadingStatsUseCase: GetReadingStatsUseCase
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 观察用户资料
            launch {
                getUserProfileUseCase().collect { profile ->
                    _uiState.update { it.copy(userProfile = profile) }
                }
            }

            // 观察阅读统计
            launch {
                getReadingStatsUseCase().collect { stats ->
                    _uiState.update { it.copy(readingStats = stats, isLoading = false) }
                }
            }
        }
    }

    fun onProfileCardClick() {
        viewModelScope.launch {
            _event.send(ProfileUiEvent.ShowSnackbar("个人资料功能开发中"))
        }
    }

    fun onSettingsClick() {
        viewModelScope.launch {
            _event.send(ProfileUiEvent.ShowSnackbar("设置功能开发中"))
        }
    }

    fun onBookmarksClick() {
        viewModelScope.launch {
            _event.send(ProfileUiEvent.NavigateToBookmarks)
        }
    }

    fun onHistoryClick() {
        viewModelScope.launch {
            _event.send(ProfileUiEvent.ShowSnackbar("阅读记录功能开发中"))
        }
    }

    fun onDownloadsClick() {
        viewModelScope.launch {
            _event.send(ProfileUiEvent.ShowSnackbar("离线下载功能开发中"))
        }
    }

    fun onDarkModeClick() {
        viewModelScope.launch {
            _event.send(ProfileUiEvent.ShowSnackbar("夜间模式功能开发中"))
        }
    }
}
