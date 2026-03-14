package com.vonfly.read.ui.screen.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.theme.ForegroundTertiary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 阅读器底部状态栏
 *
 * 显示当前时间和页码。
 *
 * 实现对照表：
 * - Footer 高度: 24 dp
 * - 水平内边距: 24 dp
 * - 底部内边距: 8 dp
 * - 时间/页码字号: 12 sp
 * - 时间/页码颜色: $--foreground-tertiary
 */
@Composable
fun ReaderFooter(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(formatTime(System.currentTimeMillis())) }

    // 每分钟更新一次时间
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            currentTime = formatTime(System.currentTimeMillis())
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 当前时间
        Text(
            text = currentTime,
            fontSize = 12.sp,
            color = ForegroundTertiary
        )

        // 页码
        Text(
            text = "$currentPage / $totalPages",
            fontSize = 12.sp,
            color = ForegroundTertiary
        )
    }
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
