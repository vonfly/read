package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 阅读器顶部控制栏
 *
 * 设计稿样式:
 * - 背景: #F9F9F9CC (半透明浅灰色 + 模糊效果)
 * - 高度: 72dp
 * - 左侧: 返回按钮 + 书名
 * - 右侧: 更多操作按钮
 */
@Composable
fun ReaderTopBar(
    chapterTitle: String?,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xCCF9F9F9)) // #F9F9F9CC
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 返回按钮
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "返回",
            tint = Color(0xFF1A1A1A), // $--foreground
            modifier = Modifier
                .clickable(onClick = onBackClick)
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 书名/章节标题
        Text(
            text = chapterTitle ?: "阅读",
            color = Color(0xFF1A1A1A), // $--foreground
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        // 更多操作按钮
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "更多",
            tint = Color(0xFF1A1A1A), // $--foreground
            modifier = Modifier
                .clickable(onClick = onMenuClick)
                .padding(4.dp)
        )
    }
}
