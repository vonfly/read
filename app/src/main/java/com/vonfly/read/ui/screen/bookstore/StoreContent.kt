package com.vonfly.read.ui.screen.bookstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.Banner
import com.vonfly.read.domain.model.Category
import com.vonfly.read.domain.model.StoreBook
import com.vonfly.read.ui.component.BannerPager
import com.vonfly.read.ui.component.BookRankItem
import com.vonfly.read.ui.component.CategoryChips
import com.vonfly.read.ui.component.SearchBar
import com.vonfly.read.ui.component.SectionHeader
import com.vonfly.read.ui.component.TabBar
import com.vonfly.read.ui.theme.Background
import com.vonfly.read.ui.theme.Foreground
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun StoreContent(
    uiState: StoreUiState,
    banners: ImmutableList<Banner>,
    categories: ImmutableList<Category>,
    hotBooks: ImmutableList<StoreBook>,
    onSearchClick: () -> Unit,
    onCategoryClick: (Int) -> Unit,
    onBannerClick: (Banner) -> Unit,
    onBookClick: (String) -> Unit,
    onMoreClick: () -> Unit,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            TabBar(
                selectedIndex = 1,
                onTabClick = onTabClick
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            // 页面标题
            Text(
                text = "书城",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Foreground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 搜索栏
                item {
                    SearchBar(
                        placeholder = "搜索书名、作者",
                        onClick = onSearchClick
                    )
                }

                // 分类标签
                item {
                    CategoryChips(
                        categories = categories.map { it.name },
                        selectedIndex = uiState.selectedCategoryIndex,
                        onCategoryClick = onCategoryClick
                    )
                }

                // Banner 轮播
                item {
                    BannerPager(
                        banners = banners,
                        onBannerClick = onBannerClick
                    )
                }

                // 热门榜单标题
                item {
                    SectionHeader(
                        title = "热门榜单",
                        showMore = true,
                        onMoreClick = onMoreClick
                    )
                }

                // 书籍列表
                items(
                    items = hotBooks,
                    key = { it.id }
                ) { book ->
                    BookRankItem(
                        book = book,
                        onClick = { onBookClick(book.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreContentPreview() {
    StoreContent(
        uiState = StoreUiState(),
        banners = persistentListOf(
            Banner(
                id = "1",
                title = "限时免费",
                description = "精选好书限时免费阅读",
                buttonText = "立即查看",
                gradientStart = "#667eea",
                gradientEnd = "#764ba2"
            )
        ),
        categories = persistentListOf(
            Category(id = "recommend", name = "推荐"),
            Category(id = "novel", name = "小说"),
            Category(id = "literature", name = "文学")
        ),
        hotBooks = persistentListOf(
            StoreBook(
                id = "1",
                rank = 1,
                title = "三体",
                author = "刘慈欣",
                rating = 9.4f,
                readCount = "892万人读过",
                coverGradientStart = "#667eea",
                coverGradientEnd = "#764ba2"
            )
        ),
        onSearchClick = {},
        onCategoryClick = {},
        onBannerClick = {},
        onBookClick = {},
        onMoreClick = {},
        onTabClick = {}
    )
}
