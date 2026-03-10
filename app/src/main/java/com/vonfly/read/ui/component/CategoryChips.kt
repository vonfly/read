package com.vonfly.read.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.Primary

/**
 * 分类标签组件
 *
 * 横向滚动分类列表，支持选中状态切换。
 *
 * @param categories 分类名称列表
 * @param selectedIndex 当前选中索引
 * @param onCategoryClick 分类点击回调
 * @param modifier Modifier
 */
@Composable
fun CategoryChips(
    categories: List<String>,
    selectedIndex: Int,
    onCategoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        itemsIndexed(categories) { index, category ->
            val isSelected = index == selectedIndex
            CategoryChip(
                text = category,
                isSelected = isSelected,
                onClick = { onCategoryClick(index) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        Primary
    } else {
        androidx.compose.ui.graphics.Color(0xFFF2F2F7)
    }
    val textColor = if (isSelected) {
        androidx.compose.ui.graphics.Color.White
    } else {
        Foreground
    }
    val fontWeight = if (isSelected) {
        FontWeight.Medium
    } else {
        FontWeight.Normal
    }

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipsPreview() {
    CategoryChips(
        categories = listOf("推荐", "小说", "文学", "历史", "科技"),
        selectedIndex = 0,
        onCategoryClick = {}
    )
}
