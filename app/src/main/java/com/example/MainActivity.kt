package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppScreen
import com.example.ui.components.ExitDialog
import com.example.ui.components.FooterBar
import com.example.ui.components.HeaderBar
import com.example.ui.components.VoiceInputDialog
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OfflineScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UrlInputScreen
import com.example.ui.theme.NiKTVTheme
import com.example.ui.theme.NikBackground
import com.example.ui.theme.NikCardBackground
import com.example.ui.theme.NikOnPrimary
import com.example.ui.theme.NikPrimary
import com.example.viewmodel.NiKTVViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NiKTVTheme {
                NiKTVApp(onFinishApp = { finish() })
            }
        }
    }
}

@Composable
fun NiKTVApp(
    onFinishApp: () -> Unit,
    viewModel: NiKTVViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeStreamUrl by viewModel.activeStreamUrl.collectAsState()
    val urlInputText by viewModel.urlInputText.collectAsState()
    val isLoadingStream by viewModel.isLoadingStream.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val showExitDialog by viewModel.showExitDialog.collectAsState()
    val showVoiceDialog by viewModel.showVoiceDialog.collectAsState()
    val isFullscreen by viewModel.isFullscreenStream.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val recentlyViewed by viewModel.recentlyViewed.collectAsState()
    val favoriteChannels by viewModel.favoriteChannels.collectAsState()
    val downloads by viewModel.downloads.collectAsState()
    val settingsState by viewModel.settingsState.collectAsState()

    // Android TV & Phone Back button handling
    BackHandler {
        if (showExitDialog) {
            viewModel.toggleExitDialog(false)
        } else if (isFullscreen) {
            viewModel.toggleFullscreen(false)
        } else if (currentScreen != AppScreen.HOME) {
            viewModel.navigateTo(AppScreen.HOME)
        } else {
            viewModel.toggleExitDialog(true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NikBackground,
        topBar = {
            if (!isFullscreen) {
                HeaderBar(
                    currentScreen = currentScreen,
                    isLoading = isLoadingStream,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onGoBack = { viewModel.goBack() },
                    onGoForward = { viewModel.goForward() },
                    onRefresh = { viewModel.refreshCurrentScreen() },
                    onExitClick = { viewModel.toggleExitDialog(true) },
                    onVoiceSearchClick = { viewModel.toggleVoiceDialog(true) }
                )
            }
        },
        bottomBar = {
            if (!isFullscreen) {
                FooterBar(
                    isConnected = isConnected,
                    activeUrl = activeStreamUrl,
                    onToggleConnection = { viewModel.toggleConnectionSimulation() }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (isFullscreen) Modifier else Modifier.padding(innerPadding))
        ) {
            // Main Screen Routing
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        activeUrl = activeStreamUrl,
                        recentlyViewedList = recentlyViewed,
                        favoriteChannelsList = favoriteChannels,
                        isFullscreen = isFullscreen,
                        onOpenUrl = { url -> viewModel.openStreamUrl(url) },
                        onToggleFavorite = { id -> viewModel.toggleFavorite(id) },
                        onClearRecentlyViewed = { viewModel.clearRecentlyViewed() },
                        onToggleFullscreen = { fs -> viewModel.toggleFullscreen(fs) },
                        onNavigateToUrlInput = { viewModel.navigateTo(AppScreen.URL_INPUT) }
                    )
                }

                AppScreen.URL_INPUT -> {
                    UrlInputScreen(
                        currentUrlInput = urlInputText,
                        onUrlInputChange = { text -> viewModel.setUrlInputText(text) },
                        onAppendChar = { charStr -> viewModel.appendUrlInputText(charStr) },
                        onBackspace = { viewModel.backspaceUrlInputText() },
                        onClear = { viewModel.clearUrlInputText() },
                        onOpenStreamUrl = { url -> viewModel.openStreamUrl(url) },
                        onVoiceInputClick = { viewModel.toggleVoiceDialog(true) }
                    )
                }

                AppScreen.SETTINGS -> {
                    SettingsScreen(
                        settingsState = settingsState,
                        onUpdateSettings = { transform -> viewModel.updateSettings(transform) },
                        onClearCache = { viewModel.clearCache() }
                    )
                }

                AppScreen.DOWNLOADS -> {
                    DownloadsScreen(
                        downloadsList = downloads,
                        onDeleteDownload = { id -> viewModel.deleteDownload(id) },
                        onOpenItem = { item -> viewModel.openStreamUrl("file://${item.title}") }
                    )
                }

                AppScreen.OFFLINE_ERROR -> {
                    OfflineScreen(
                        onRetry = {
                            viewModel.toggleConnectionSimulation()
                            viewModel.navigateTo(AppScreen.HOME)
                        },
                        onOpenSettings = { viewModel.navigateTo(AppScreen.SETTINGS) }
                    )
                }
            }

            // Animated Toast Notification Pill
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) {
                toastMessage?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = NikCardBackground,
                        shadowElevation = 8.dp
                    ) {
                        Text(
                            text = msg,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NikOnPrimary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(NikPrimary)
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }

    // Exit Confirmation Dialog Overlay
    if (showExitDialog) {
        ExitDialog(
            onDismiss = { viewModel.toggleExitDialog(false) },
            onConfirmExit = onFinishApp
        )
    }

    // Voice Assistant Input Dialog Overlay (TV & Phone)
    if (showVoiceDialog) {
        VoiceInputDialog(
            onDismiss = { viewModel.toggleVoiceDialog(false) },
            onResult = { resultText -> viewModel.processVoiceCommand(resultText) }
        )
    }
}
