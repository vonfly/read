package com.vonfly.read.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vonfly.read.domain.model.ReadingStats
import com.vonfly.read.domain.model.UserProfile
import com.vonfly.read.ui.component.TabBar
import com.vonfly.read.ui.theme.Accent
import com.vonfly.read.ui.theme.AppTheme
import com.vonfly.read.ui.theme.Background
import com.vonfly.read.ui.theme.Card
import com.vonfly.read.ui.theme.Foreground
import com.vonfly.read.ui.theme.ForegroundSecondary
import com.vonfly.read.ui.theme.ForegroundTertiary
import com.vonfly.read.ui.theme.NightModeIcon
import com.vonfly.read.ui.theme.Primary
import com.vonfly.read.ui.theme.Success
import com.vonfly.read.ui.theme.Spacing

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onProfileCardClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            TabBar(
                selectedIndex = 2,
                onTabClick = onTabClick
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header
            ProfileHeader(onSettingsClick = onSettingsClick)

            // Content Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.Lg)
                    .padding(top = Spacing.Lg),
                verticalArrangement = Arrangement.spacedBy(Spacing.Lg)
            ) {
                // User Card
                UserCard(
                    userProfile = uiState.userProfile,
                    onClick = onProfileCardClick
                )

                // Stats Card
                StatsCard(readingStats = uiState.readingStats)

                // Menu Section
                MenuSection(
                    onBookmarksClick = onBookmarksClick,
                    onHistoryClick = onHistoryClick,
                    onDownloadsClick = onDownloadsClick,
                    onDarkModeClick = onDarkModeClick
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = Spacing.Lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "我的",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Foreground
        )

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "设置",
                modifier = Modifier.size(22.dp),
                tint = Foreground
            )
        }
    }
}

// 阴影颜色: 7% 不透明度黑色（在 3% 设计稿基础上微调）
private val CardShadowColor = Color(0x12000000)

@Composable
private fun UserCard(
    userProfile: UserProfile,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 8f
                shape = cardShape
                spotShadowColor = CardShadowColor
                ambientShadowColor = CardShadowColor
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = cardShape,
        color = Card,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2)
                            )
                        )
                    ),
            )

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = userProfile.nickname,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Foreground
                )
                Text(
                    text = userProfile.userId,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = ForegroundSecondary
                )
            }

            // Chevron
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ForegroundTertiary
            )
        }
    }
}

@Composable
private fun StatsCard(
    readingStats: ReadingStats
) {
    val cardShape = RoundedCornerShape(16.dp)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                shadowElevation = 8f
                shape = cardShape
                spotShadowColor = CardShadowColor
                ambientShadowColor = CardShadowColor
            },
        shape = cardShape,
        color = Card,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(
                value = readingStats.booksRead.toString(),
                label = "已读书籍",
                valueColor = Primary
            )
            StatItem(
                value = readingStats.readingHours.toString(),
                label = "阅读小时",
                valueColor = Accent
            )
            StatItem(
                value = readingStats.booksFavorited.toString(),
                label = "收藏书籍",
                valueColor = Success
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = ForegroundSecondary
        )
    }
}

@Composable
private fun MenuSection(
    onBookmarksClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onDarkModeClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Card,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            MenuItem(
                icon = Icons.Outlined.Bookmark,
                iconColor = Primary,
                label = "我的书签",
                onClick = onBookmarksClick
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.History,
                iconColor = Accent,
                label = "阅读记录",
                onClick = onHistoryClick
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.Download,
                iconColor = Success,
                label = "离线下载",
                onClick = onDownloadsClick
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            MenuItem(
                icon = Icons.Outlined.DarkMode,
                iconColor = NightModeIcon,
                label = "夜间模式",
                onClick = onDarkModeClick
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconColor
            )
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = Foreground
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ForegroundTertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    AppTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isLoading = false,
                userProfile = UserProfile("阅读爱好者", "ID: 12345678"),
                readingStats = ReadingStats(128, 256, 32)
            ),
            onProfileCardClick = {},
            onSettingsClick = {},
            onBookmarksClick = {},
            onHistoryClick = {},
            onDownloadsClick = {},
            onDarkModeClick = {},
            onTabClick = {}
        )
    }
}
