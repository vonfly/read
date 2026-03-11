package com.vonfly.read.ui.screen.bookdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.Primary
import com.vonfly.read.ui.theme.Success

@Composable
fun BottomActionBar(
    isInShelf: Boolean,
    isAddingToShelf: Boolean,
    onAddToShelfClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onReadNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Card)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Add to Shelf Button
        ActionButton(
            icon = if (isInShelf) Icons.Filled.Check else Icons.Outlined.Add,
            contentDescription = if (isInShelf) "已在书架" else "加入书架",
            isEnabled = !isInShelf && !isAddingToShelf,
            tint = if (isInShelf) Success else ForegroundSecondary,
            onClick = onAddToShelfClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Download Button
        ActionButton(
            icon = Icons.Outlined.Download,
            contentDescription = "下载",
            isEnabled = true,
            tint = ForegroundSecondary,
            onClick = onDownloadClick
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Read Now Button
        Button(
            onClick = onReadNowClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            )
        ) {
            Text(
                text = "立即阅读",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    isEnabled: Boolean,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (isEnabled) Card.copy(alpha = 0.8f) else Card.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            enabled = isEnabled,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp),
                tint = tint
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomActionBarPreview() {
    AppTheme {
        BottomActionBar(
            isInShelf = false,
            isAddingToShelf = false,
            onAddToShelfClick = {},
            onDownloadClick = {},
            onReadNowClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomActionBarInShelfPreview() {
    AppTheme {
        BottomActionBar(
            isInShelf = true,
            isAddingToShelf = false,
            onAddToShelfClick = {},
            onDownloadClick = {},
            onReadNowClick = {}
        )
    }
}
