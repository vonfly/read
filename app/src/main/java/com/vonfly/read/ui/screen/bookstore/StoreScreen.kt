package com.vonfly.read.ui.screen.bookstore

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vonfly.read.ui.theme.Background

@Composable
fun StoreScreen(
    onNavigateToBookDetail: (String) -> Unit,
    onNavigateToBookList: () -> Unit,
    viewModel: StoreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val banners by viewModel.banners.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val hotBooks by viewModel.hotBooks.collectAsStateWithLifecycle()
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is StoreUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is StoreUiEvent.NavigateToBookDetail -> onNavigateToBookDetail(event.bookId)
            }
        }
    }

    StoreContent(
        uiState = uiState,
        banners = banners,
        categories = categories,
        hotBooks = hotBooks,
        onSearchClick = viewModel::onSearchClick,
        onCategoryClick = viewModel::onCategoryClick,
        onBannerClick = viewModel::onBannerClick,
        onBookClick = viewModel::onBookClick,
        onMoreClick = viewModel::onMoreClick,
        onTabClick = { tabIndex ->
            when (tabIndex) {
                0 -> onNavigateToBookList()
                // 2 -> onNavigateToProfile() // TODO: 我的页面
            }
        },
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    )
}
