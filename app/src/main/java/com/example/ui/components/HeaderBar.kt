package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.ui.theme.NikBorder
import com.example.ui.theme.NikCardBackground
import com.example.ui.theme.NikFocusGlow
import com.example.ui.theme.NikOnPrimary
import com.example.ui.theme.NikPrimary
import com.example.ui.theme.NikSurfaceVariant
import com.example.ui.theme.NikTertiary

@Composable
fun HeaderBar(
    currentScreen: AppScreen,
    isLoading: Boolean,
    onNavigate: (AppScreen) -> Unit,
    onGoBack: () -> Unit,
    onGoForward: () -> Unit,
    onRefresh: () -> Unit,
    onExitClick: () -> Unit,
    onVoiceSearchClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = NikCardBackground,
        tonalElevation = 4.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Brand Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigate(AppScreen.HOME) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NikPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = "NiK TV Logo",
                            tint = NikOnPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "NiK TV",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "ENGINE v4.2",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NikTertiary,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Center Navigation Controls (TV & Mobile D-pad accessible)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(NikSurfaceVariant.copy(alpha = 0.6f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    NavIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        onClick = onGoBack
                    )

                    NavIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward",
                        onClick = onGoForward
                    )

                    NavIconButton(
                        icon = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        onClick = onRefresh
                    )

                    NavIconButton(
                        icon = Icons.Default.Mic,
                        contentDescription = "Voice Search & Command",
                        onClick = onVoiceSearchClick
                    )

                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(NikBorder))

                    NavIconButton(
                        icon = Icons.Default.Home,
                        contentDescription = "Home",
                        isSelected = currentScreen == AppScreen.HOME,
                        onClick = { onNavigate(AppScreen.HOME) }
                    )

                    NavIconButton(
                        icon = Icons.Default.Link,
                        contentDescription = "Enter Stream URL",
                        isSelected = currentScreen == AppScreen.URL_INPUT,
                        onClick = { onNavigate(AppScreen.URL_INPUT) }
                    )

                    NavIconButton(
                        icon = Icons.Default.Download,
                        contentDescription = "Downloads",
                        isSelected = currentScreen == AppScreen.DOWNLOADS,
                        onClick = { onNavigate(AppScreen.DOWNLOADS) }
                    )

                    NavIconButton(
                        icon = Icons.Default.Settings,
                        contentDescription = "Settings",
                        isSelected = currentScreen == AppScreen.SETTINGS,
                        onClick = { onNavigate(AppScreen.SETTINGS) }
                    )
                }

                // Right Profile & Power Exit Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Profile Chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NikSurfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(NikPrimary.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Profile",
                                tint = NikPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PRO USER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Power / Exit button
                    IconButton(
                        onClick = onExitClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Exit App",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Animated top loading bar indicator
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = NikPrimary,
                    trackColor = Color.Transparent
                )
            }
        }
    }
}

@Composable
fun NavIconButton(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isFocused -> NikFocusGlow.copy(alpha = 0.3f)
                    isSelected -> NikPrimary
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) NikFocusGlow else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = when {
                isSelected -> NikOnPrimary
                isFocused -> Color.White
                else -> Color.White.copy(alpha = 0.7f)
            },
            modifier = Modifier.size(20.dp)
        )
    }
}
