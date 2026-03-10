package com.vonfly.read.ui.screen.bookstore

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonfly.read.domain.model.Banner
import com.vonfly.read.domain.model.Category
import com.vonfly.read.domain.model.StoreBook
import com.vonfly.read.domain.usecase.GetBannersUseCase
import com.vonfly.read.domain.usecase.GetCategoriesUseCase
import com.vonfly.read.domain.usecase.GetHotBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Immutable
data class StoreUiState(
    val selectedCategoryIndex: Int = 0,
    val isLoading: Boolean = false
)

sealed class StoreUiEvent {
    data class ShowSnackbar(val message: String) : StoreUiEvent()
    data class NavigateToBookDetail(val bookId: String) : StoreUiEvent()
}

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class StoreViewModel @Inject constructor(
    getBannersUseCase: GetBannersUseCase,
    getCategoriesUseCase: GetCategoriesUseCase,
    private val getHotBooksUseCase: GetHotBooksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    private val _event = Channel<StoreUiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    // 分类 ID 变化流
    private val selectedCategoryIdFlow = MutableStateFlow<String?>(null)

    // Banner 列表
    val banners: StateFlow<ImmutableList<Banner>> = getBannersUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = persistentListOf()
        )

    // 分类列表
    val categories: StateFlow<ImmutableList<Category>> = getCategoriesUseCase()
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = persistentListOf()
        )

    // 热门书籍列表（响应分类变化）
    val hotBooks: StateFlow<ImmutableList<StoreBook>> = selectedCategoryIdFlow
        .flatMapLatest { categoryId ->
            getHotBooksUseCase(categoryId)
        }
        .map { it.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = persistentListOf()
        )

    fun onCategoryClick(index: Int) {
        if (index == _uiState.value.selectedCategoryIndex) return
        _uiState.update { it.copy(selectedCategoryIndex = index) }

        // 计算分类 ID：index 0 表示推荐（null），其他为具体分类 ID
        val categoryId = if (index == 0) {
            null
        } else {
            categories.value.getOrNull(index)?.id
        }
        selectedCategoryIdFlow.value = categoryId
    }

    fun onSearchClick() {
        viewModelScope.launch {
            _event.send(StoreUiEvent.ShowSnackbar("搜索功能开发中"))
        }
    }

    fun onBannerClick(banner: Banner) {
        viewModelScope.launch {
            _event.send(StoreUiEvent.ShowSnackbar("活动：${banner.title}"))
        }
    }

    fun onBookClick(bookId: String) {
        viewModelScope.launch {
            _event.send(StoreUiEvent.NavigateToBookDetail(bookId))
        }
    }

    fun onMoreClick() {
        viewModelScope.launch {
            _event.send(StoreUiEvent.ShowSnackbar("更多榜单开发中"))
        }
    }
}
