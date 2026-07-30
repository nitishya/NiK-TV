package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NikBorder
import com.example.ui.theme.NikCardBackground
import com.example.ui.theme.NikFocusGlow
import com.example.ui.theme.NikOnPrimary
import com.example.ui.theme.NikPrimary
import com.example.ui.theme.NikSurface
import com.example.ui.theme.NikSurfaceVariant
import com.example.ui.theme.NikTertiary
import com.example.ui.theme.NikTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UrlInputScreen(
    currentUrlInput: String,
    onUrlInputChange: (String) -> Unit,
    onAppendChar: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onOpenStreamUrl: (String) -> Unit,
    onVoiceInputClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val recentDestinations = listOf(
        "https://stream.nexus-global.io/dashboard",
        "https://niktv.io/stream/news",
        "https://niktv.io/stream/sports",
        "https://twitch.tv/hub",
        "https://youtube.com/live"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NikSurface)
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
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
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = NikOnPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "CONNECT TO SOURCE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Enter web video URL, M3U8 IPTV link, or website address",
                    fontSize = 12.sp,
                    color = NikTextSecondary
                )
            }
        }

        // Input Box Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NikCardBackground),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NikBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = currentUrlInput,
                    onValueChange = onUrlInputChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("https://example.com/stream.m3u8", color = NikTextSecondary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = NikPrimary
                        )
                    },
                    trailingIcon = {
                        if (currentUrlInput.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Input",
                                tint = NikTextSecondary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable { onClear() }
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NikSurfaceVariant,
                        unfocusedContainerColor = NikSurfaceVariant.copy(alpha = 0.5f),
                        focusedBorderColor = NikPrimary,
                        unfocusedBorderColor = NikBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onVoiceInputClick,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NikTertiary, contentColor = NikSurface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("VOICE", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onOpenStreamUrl(currentUrlInput) },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NikPrimary, contentColor = NikOnPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OPEN STREAM", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            onUrlInputChange("https://niktv.io/stream/news")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("PASTE URL", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onClear,
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NikTextSecondary)
                    ) {
                        Text("CLEAR", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Recent Destinations Chips
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = NikTertiary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Recent Destinations",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                recentDestinations.forEach { dest ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = NikCardBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NikBorder),
                        modifier = Modifier.clickable {
                            onUrlInputChange(dest)
                            onOpenStreamUrl(dest)
                        }
                    ) {
                        Text(
                            text = dest.removePrefix("https://"),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = NikPrimary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Interactive TV On-Screen Keyboard
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NikCardBackground),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NikBorder, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ON-SCREEN TV KEYBOARD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NikTextSecondary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Number row
                KeyboardRow(
                    keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
                    onKeyClick = onAppendChar
                )

                Spacer(modifier = Modifier.height(6.dp))

                // QWERTY Row 1
                KeyboardRow(
                    keys = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
                    onKeyClick = onAppendChar
                )

                Spacer(modifier = Modifier.height(6.dp))

                // QWERTY Row 2
                KeyboardRow(
                    keys = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l", "/"),
                    onKeyClick = onAppendChar
                )

                Spacer(modifier = Modifier.height(6.dp))

                // QWERTY Row 3
                KeyboardRow(
                    keys = listOf("z", "x", "c", "v", "b", "n", "m", ".", ":", "-"),
                    onKeyClick = onAppendChar
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Action Row (VOICE, http://, .com, SPACE, BACKSPACE, DONE)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TvKeyButton(
                        icon = Icons.Default.Mic,
                        text = "VOICE",
                        modifier = Modifier.weight(1.2f),
                        isHighlight = true,
                        onClick = onVoiceInputClick
                    )

                    TvKeyButton(
                        text = "https://",
                        modifier = Modifier.weight(1.2f),
                        onClick = { onAppendChar("https://") }
                    )

                    TvKeyButton(
                        text = ".com",
                        modifier = Modifier.weight(1f),
                        onClick = { onAppendChar(".com") }
                    )

                    TvKeyButton(
                        text = ".m3u8",
                        modifier = Modifier.weight(1f),
                        onClick = { onAppendChar(".m3u8") }
                    )

                    TvKeyButton(
                        text = "SPACE",
                        modifier = Modifier.weight(1.5f),
                        onClick = { onAppendChar(" ") }
                    )

                    TvKeyButton(
                        icon = Icons.AutoMirrored.Filled.Backspace,
                        modifier = Modifier.weight(1f),
                        isActionKey = true,
                        onClick = onBackspace
                    )

                    TvKeyButton(
                        text = "DONE",
                        modifier = Modifier.weight(1.2f),
                        isHighlight = true,
                        onClick = { onOpenStreamUrl(currentUrlInput) }
                    )
                }
            }
        }
    }
}

@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { key ->
            TvKeyButton(
                text = key,
                modifier = Modifier.weight(1f),
                onClick = { onKeyClick(key) }
            )
        }
    }
}

@Composable
fun TvKeyButton(
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isActionKey: Boolean = false,
    isHighlight: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isFocused -> NikFocusGlow
                    isHighlight -> NikPrimary
                    isActionKey -> NikSurfaceVariant
                    else -> NikSurfaceVariant.copy(alpha = 0.6f)
                }
            )
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) NikFocusGlow else NikBorder,
                shape = RoundedCornerShape(8.dp)
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isHighlight || isFocused -> NikOnPrimary
                    else -> Color.White.copy(alpha = 0.9f)
                }
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isHighlight || isFocused) NikOnPrimary else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
