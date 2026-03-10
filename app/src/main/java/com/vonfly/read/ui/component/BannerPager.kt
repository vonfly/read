package com.vonfly.read.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vonfly.read.domain.model.Banner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Banner 轮播容器组件
 *
 * 自动轮播 Banner 列表，支持手动滑动。
 *
 * @param banners Banner 列表
 * @param onBannerClick Banner 点击回调
 * @param modifier Modifier
 * @param autoScrollDelay 自动轮播间隔（毫秒），默认 3 秒
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerPager(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollDelay: Long = 3000L
) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })
    val coroutineScope = rememberCoroutineScope()

    // 自动轮播
    LaunchedEffect(pagerState.currentPage) {
        delay(autoScrollDelay)
        val nextPage = (pagerState.currentPage + 1) % banners.size
        coroutineScope.launch {
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = modifier) {
        // Banner 滑动容器
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            BannerCard(
                banner = banners[page],
                onClick = { onBannerClick(banners[page]) }
            )
        }

        // 底部指示器
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .height(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(banners.size) { index ->
                val isSelected = index == pagerState.currentPage
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .background(
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BannerPagerPreview() {
    BannerPager(
        banners = listOf(
            Banner(
                id = "1",
                title = "限时免费",
                description = "精选好书限时免费阅读",
                buttonText = "立即查看",
                gradientStart = "#667eea",
                gradientEnd = "#764ba2"
            ),
            Banner(
                id = "2",
                title = "新人专享",
                description = "首单立减 10 元",
                buttonText = "立即领取",
                gradientStart = "#f093fb",
                gradientEnd = "#f5576c"
            )
        ),
        onBannerClick = {}
    )
}
