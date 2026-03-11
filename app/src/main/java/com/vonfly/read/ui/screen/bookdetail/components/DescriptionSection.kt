package com.vonfly.read.ui.screen.bookdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.Primary

@Composable
fun DescriptionSection(
    description: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Title
        Text(
            text = "简介",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Foreground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description Text
        Text(
            text = description,
            fontSize = 14.sp,
            lineHeight = 22.4.sp, // 1.6x
            color = Foreground,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Expand/Collapse Toggle
        Text(
            text = if (isExpanded) "收起" else "展开全部",
            fontSize = 14.sp,
            color = Primary,
            modifier = Modifier.clickable(onClick = onToggleExpand)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DescriptionSectionPreview() {
    AppTheme {
        DescriptionSection(
            description = "这是一部关于人类文明与外星文明博弈的科幻史诗。文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划「红岸工程」取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。",
            isExpanded = false,
            onToggleExpand = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DescriptionSectionExpandedPreview() {
    AppTheme {
        DescriptionSection(
            description = "这是一部关于人类文明与外星文明博弈的科幻史诗。文化大革命如火如荼进行的同时，军方探寻外星文明的绝秘计划「红岸工程」取得了突破性进展。但在按下发射键的那一刻，历经劫难的叶文洁没有意识到，她彻底改变了人类的命运。地球文明向宇宙发出的第一声啼鸣，以太阳为中心，以光速向宇宙深处飞驰……",
            isExpanded = true,
            onToggleExpand = {}
        )
    }
}
