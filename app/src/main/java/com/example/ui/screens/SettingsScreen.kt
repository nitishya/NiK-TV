package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SettingsState
import com.example.ui.theme.NikBorder
import com.example.ui.theme.NikCardBackground
import com.example.ui.theme.NikFocusGlow
import com.example.ui.theme.NikOnPrimary
import com.example.ui.theme.NikPrimary
import com.example.ui.theme.NikSurface
import com.example.ui.theme.NikSurfaceVariant
import com.example.ui.theme.NikTertiary
import com.example.ui.theme.NikTextSecondary

@Composable
fun SettingsScreen(
    settingsState: SettingsState,
    onUpdateSettings: ((SettingsState) -> SettingsState) -> Unit,
    onClearCache: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("General") }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NikSurface)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NikPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = NikOnPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "SYSTEM CONFIGURATION",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Manage player behavior, network proxies, storage and TV display options",
                    fontSize = 12.sp,
                    color = NikTextSecondary
                )
            }
        }

        // Split View: Left Sidebar Navigation & Right Content Panel
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Category Navigation Sidebar
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NikCardBackground),
                modifier = Modifier
                    .width(220.dp)
                    .border(1.dp, NikBorder, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategorySidebarItem(
                        icon = Icons.Default.Settings,
                        label = "General",
                        isSelected = selectedCategory == "General",
                        onClick = { selectedCategory = "General" }
                    )
                    CategorySidebarItem(
                        icon = Icons.Default.Language,
                        label = "Browsing",
                        isSelected = selectedCategory == "Browsing",
                        onClick = { selectedCategory = "Browsing" }
                    )
                    CategorySidebarItem(
                        icon = Icons.Default.Palette,
                        label = "Appearance",
                        isSelected = selectedCategory == "Appearance",
                        onClick = { selectedCategory = "Appearance" }
                    )
                    CategorySidebarItem(
                        icon = Icons.Default.Storage,
                        label = "Storage",
                        isSelected = selectedCategory == "Storage",
                        onClick = { selectedCategory = "Storage" }
                    )
                    CategorySidebarItem(
                        icon = Icons.Default.Info,
                        label = "About",
                        isSelected = selectedCategory == "About",
                        onClick = { selectedCategory = "About" }
                    )
                }
            }

            // Right Settings Details Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NikCardBackground),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, NikBorder, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (selectedCategory) {
                        "General" -> {
                            Text(
                                text = "General Configuration",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Default Home URL
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Default Startup Stream / Home URL",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                OutlinedTextField(
                                    value = settingsState.defaultHomeUrl,
                                    onValueChange = { newUrl ->
                                        onUpdateSettings { it.copy(defaultHomeUrl = newUrl) }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = NikSurfaceVariant,
                                        unfocusedContainerColor = NikSurfaceVariant.copy(alpha = 0.5f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            // Auto-Update Switch
                            SettingToggleRow(
                                title = "Auto-Update Player Engine",
                                subtitle = "Automatically download and install core codecs & updates in background",
                                isChecked = settingsState.autoUpdateClient,
                                onCheckedChange = { checked ->
                                    onUpdateSettings { it.copy(autoUpdateClient = checked) }
                                }
                            )
                        }

                        "Browsing" -> {
                            Text(
                                text = "Web Engine & Security",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            SettingToggleRow(
                                title = "Enable JavaScript Engine",
                                subtitle = "Required for interactive live web players and HTML script execution",
                                isChecked = settingsState.enableJavaScript,
                                onCheckedChange = { checked ->
                                    onUpdateSettings { it.copy(enableJavaScript = checked) }
                                }
                            )

                            SettingToggleRow(
                                title = "Accept Third-Party Cookies",
                                subtitle = "Allow external auth tokens for protected media streams",
                                isChecked = settingsState.acceptThirdPartyCookies,
                                onCheckedChange = { checked ->
                                    onUpdateSettings { it.copy(acceptThirdPartyCookies = checked) }
                                }
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Download Save Directory",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = NikTertiary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = settingsState.downloadLocation,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = NikTextSecondary
                                    )
                                }
                            }
                        }

                        "Appearance" -> {
                            Text(
                                text = "Display & TV Interface",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Active Theme Preset",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    listOf("Midnight Slate", "Deep OLED", "Electric Cyan").forEach { themeName ->
                                        val isSelected = settingsState.activeTheme == themeName
                                        OutlinedButton(
                                            onClick = {
                                                onUpdateSettings { it.copy(activeTheme = themeName) }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = if (isSelected) NikPrimary else Color.Transparent,
                                                contentColor = if (isSelected) NikOnPrimary else Color.White
                                            )
                                        ) {
                                            Text(themeName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        "Storage" -> {
                            Text(
                                text = "Storage & Cache Management",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = NikSurfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Cache Space Allocated", fontSize = 13.sp, color = Color.White)
                                        Text(
                                            text = "${settingsState.cacheUsedMb} MB / ${settingsState.cacheTotalMb} MB",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NikTertiary
                                        )
                                    }

                                    LinearProgressIndicator(
                                        progress = { settingsState.cacheUsedMb.toFloat() / settingsState.cacheTotalMb.toFloat() },
                                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                        color = NikTertiary,
                                        trackColor = NikBorder
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Button(
                                        onClick = onClearCache,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NikPrimary, contentColor = NikOnPrimary)
                                    ) {
                                        Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("CLEAR BROWSER CACHE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        "About" -> {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(NikPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Tv, contentDescription = null, tint = NikOnPrimary, modifier = Modifier.size(36.dp))
                                }

                                Text(
                                    text = "NiK TV",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )

                                Text(
                                    text = "Version v4.2.0-stable (Build 2026)",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = NikTextSecondary
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NikTertiary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("All Systems Operational • Full TV & Phone Support", fontSize = 12.sp, color = NikTertiary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySidebarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    isFocused -> NikFocusGlow
                    isSelected -> NikPrimary
                    else -> Color.Transparent
                }
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = when {
                isSelected -> NikOnPrimary
                isFocused -> Color.White
                else -> NikTextSecondary
            },
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = when {
                isSelected -> NikOnPrimary
                isFocused -> Color.White
                else -> NikTextSecondary
            }
        )
    }
}

@Composable
fun SettingToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = subtitle, fontSize = 11.sp, color = NikTextSecondary)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NikPrimary,
                uncheckedThumbColor = NikTextSecondary,
                uncheckedTrackColor = NikSurfaceVariant
            )
        )
    }
}
