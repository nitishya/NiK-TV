package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppScreen
import com.example.model.DownloadItem
import com.example.model.DownloadType
import com.example.model.FavoriteChannel
import com.example.model.RecentlyViewedItem
import com.example.model.SettingsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NiKTVViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _previousScreen = MutableStateFlow(AppScreen.HOME)
    val previousScreen: StateFlow<AppScreen> = _previousScreen.asStateFlow()

    private val _activeStreamUrl = MutableStateFlow("https://stream.nexus-global.io/dashboard")
    val activeStreamUrl: StateFlow<String> = _activeStreamUrl.asStateFlow()

    private val _urlInputText = MutableStateFlow("https://stream.nexus-global.io/dashboard")
    val urlInputText: StateFlow<String> = _urlInputText.asStateFlow()

    private val _isLoadingStream = MutableStateFlow(false)
    val isLoadingStream: StateFlow<Boolean> = _isLoadingStream.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _showExitDialog = MutableStateFlow(false)
    val showExitDialog: StateFlow<Boolean> = _showExitDialog.asStateFlow()

    private val _showVoiceDialog = MutableStateFlow(false)
    val showVoiceDialog: StateFlow<Boolean> = _showVoiceDialog.asStateFlow()

    private val _isFullscreenStream = MutableStateFlow(false)
    val isFullscreenStream: StateFlow<Boolean> = _isFullscreenStream.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    // Recently Viewed items initialized with hotlinked images matching requested UI
    private val _recentlyViewed = MutableStateFlow(
        listOf(
            RecentlyViewedItem(
                id = "1",
                title = "Global News Stream HD",
                category = "News",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA3VqYGYz5Y3iyx7Zj08VNX31Xi4Xy47XBAujbtH_DXFRoSPyqvd85fT6TQeF2k-Q6khVDzj5AM2r3f1IiN-iiJNpMGCUJMNC7XydFLwTLOgZpiXsO9UPLEB5QOuIiABZoGVmjaf4p8lT1AwY6T6bUM85tR3to4fp85zKomzBxE6SSsuFlG2Kma9lgbOzpA5ehpVS-e67lauy2JwxHOIiC9SZQ5LRlkIOCTs0DBYXyqQVCSuE3zG5odag",
                streamUrl = "https://niktv.io/stream/news",
                isFavorite = true,
                durationOrTag = "LIVE HD"
            ),
            RecentlyViewedItem(
                id = "2",
                title = "Live Sports: Pro League",
                category = "Sports",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCkRcrmQFRtgx0rjl-Qskd2fGATtikVBe99oZCnviMcuWoO1UJhRh4Tlyqt4-YETy5FwWL8FROshuW9kMNV9YMbKbbKER6o_DYzUgry00pRMKdCwpZlYG-HJHCAypbHcJRbfxHJBRtWHFWad6_SUU6VsdI-y0WflcglkB3FPoo8JV65DN2wTViukuklPLZoCoGVQbH6YRL3DYCzQdd7smWSIx3K5uR-pm9uHqTrJoAI82Q8aFksLQfagg",
                streamUrl = "https://niktv.io/stream/sports",
                isFavorite = false,
                durationOrTag = "4K 60FPS"
            ),
            RecentlyViewedItem(
                id = "3",
                title = "Tech Talk Daily",
                category = "Podcast",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAmb1OzvLz4OA22jlzJC08x97iprVEjymOPQZTMW1oAXdD29Ez_CFdqAIrBBFt54uCoPYBwfA7YVGvA-UBjBqidhE_vWV9kbKA822YP6tZ0uHdYtA1gOatSzVYrVgRfaCYDfd70HaB_H3TZ-6zbvcz_2mzvxQF0y2uWGMI0d0WBJBzYbAquREI7so2GypBjEOlc7oTTvfrmY1hcshoqmNjHFWJbOwl5OezYN3CPJHvJC4qpU0EbSpXCUg",
                streamUrl = "https://niktv.io/stream/tech",
                isFavorite = true,
                durationOrTag = "45:12"
            ),
            RecentlyViewedItem(
                id = "4",
                title = "Documentary: The Blue Planet",
                category = "Nature",
                imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCB0aAVlCTMVsL1OYCdMXsFP6f9A7FfrQfn4PMaphjkv_O_ZZC69LegH2ZKddKw2mUkhLxVB0CVquxNNPb0LMbRSWA52VVlwFidmUiHdJb5Brr-wDzBIOhbnrbdRJz4JFT53tYuC4ZF__4epBNtf84H1ynzioioQnE2rRnUzM5Ahp7Kba9j4em79I8wRW1Snety4oA6cJWoDBRFaNZsbW9bFlOjiFARZ5uYW0LhDT3Fs1Mu0mjyHe1HKg",
                streamUrl = "https://niktv.io/stream/nature",
                isFavorite = false,
                durationOrTag = "1:24:00"
            )
        )
    )
    val recentlyViewed: StateFlow<List<RecentlyViewedItem>> = _recentlyViewed.asStateFlow()

    // Favorite Channels
    private val _favoriteChannels = MutableStateFlow(
        listOf(
            FavoriteChannel("f1", "Nexus TV", "Main Stream", "LIVE", "https://lh3.googleusercontent.com/aida-public/AB6AXuDT9PW7mg_TexgBCTW3AiPBgBz1hDGhOUilcSw8kxSx4WVzCyCpeeXybvl89NipCP7YPa60cCkrwGRA6hZVQ9LjV20xr4s0pbUIJh0IwxrPsJX43VINX1Jgi_xUZvLkn7_siX03yCi35e0EsTksZ2iGe0ooqf9cMmrHxGk0KqaTSz64ec76_07RMIPgTDyWSqi4HW9kXJsT8KuJ3WFswBVElHrp97G8G_bMZuWDmIgr6WFibOho5nMwjQ", "https://stream.nexus-global.io/dashboard"),
            FavoriteChannel("f2", "Twitch Gaming", "Gaming", "4K", "https://lh3.googleusercontent.com/aida-public/AB6AXuCkRcrmQFRtgx0rjl-Qskd2fGATtikVBe99oZCnviMcuWoO1UJhRh4Tlyqt4-YETy5FwWL8FROshuW9kMNV9YMbKbbKER6o_DYzUgry00pRMKdCwpZlYG-HJHCAypbHcJRbfxHJBRtWHFWad6_SUU6VsdI-y0WflcglkB3FPoo8JV65DN2wTViukuklPLZoCoGVQbH6YRL3DYCzQdd7smWSIx3K5uR-pm9uHqTrJoAI82Q8aFksLQfagg", "https://twitch.tv/hub"),
            FavoriteChannel("f3", "Music Live", "Entertainment", "HD", "https://lh3.googleusercontent.com/aida-public/AB6AXuAmb1OzvLz4OA22jlzJC08x97iprVEjymOPQZTMW1oAXdD29Ez_CFdqAIrBBFt54uCoPYBwfA7YVGvA-UBjBqidhE_vWV9kbKA822YP6tZ0uHdYtA1gOatSzVYrVgRfaCYDfd70HaB_H3TZ-6zbvcz_2mzvxQF0y2uWGMI0d0WBJBzYbAquREI7so2GypBjEOlc7oTTvfrmY1hcshoqmNjHFWJbOwl5OezYN3CPJHvJC4qpU0EbSpXCUg", "https://niktv.io/music")
        )
    )
    val favoriteChannels: StateFlow<List<FavoriteChannel>> = _favoriteChannels.asStateFlow()

    // Download Items
    private val _downloads = MutableStateFlow(
        listOf(
            DownloadItem("d1", "Neon_Drift_4K.mkv", "4.2 GB", DownloadType.VIDEO, "https://lh3.googleusercontent.com/aida-public/AB6AXuCJaeFQ7mEEwpUCefFPgh4BRAK8o6ThW9iSW2VDkZnMs7OP-hhFz7RE3WIuQKCUQulqoOVV5IUSKFZKu_cDtWr9sMfaaridAAMakZun-Ynbshkd9-nChP9BISlf95SuyC93ExEK4SNjQDUZdebYLZtP7yqAoYVRhaG0ecM0lGQ9dEsJ39q88EVSNK044T3bMOUwf1-_SX1f4tFZsBwRb5PhyAkC7QiV1npPvIvboc_1ncRLIWRhTJvBQw", "OPEN"),
            DownloadItem("d2", "System_Update_v2.apk", "856 MB", DownloadType.APK, "", "INSTALL"),
            DownloadItem("d3", "User_Manual_2024.pdf", "12 MB", DownloadType.DOCUMENT, "https://lh3.googleusercontent.com/aida-public/AB6AXuDFUyOAwKHfC344ffWCm-XHGtTjMVXpwWH-N4S0z1dRSyKcgkoqglIuvTdeJqFCgsFZITbtOO74spRDd0zYYYXHR7mdMpG7LB3vKnPcKRvcxZAxiZyWxdXL_-DObi8vigTyq6CBiwwTx_s4SF5IUmU4LVoi0G2UAg2KJvAxnZ_gry9aLFjM6_f7hPKRgq5xbvPuQoqZLxOsyTeeD8HaiX252Nh08nREu0vPDqStFwP3UkQpe6hraUAiNA", "READ"),
            DownloadItem("d4", "Ambient_Waves_HQ.flac", "142 MB", DownloadType.AUDIO, "", "LISTEN"),
            DownloadItem("d5", "Galactic_Horizons.mp4", "12.8 GB", DownloadType.VIDEO, "https://lh3.googleusercontent.com/aida-public/AB6AXuBo2Fg6V93LL2dVzrqJ9FRxXMTRYtLWTTrXQ2rq_thxYbUYXd4KSFaqH45OWKGHwaMwnLuYEJShJsfGmz8foJkTpBY5qt6dYXPPnsWwqvFz3MuxtNrvr1FUuFXLn7cHuS_Bp8oIhbFdASfIMeAksCyQ4w5VnMLe98VO2QjQSOafdKKS35zctzEyCmCJNuYmx5g3t6P6eErQqU3OcwvRIAhnygiiekTSwsrk7i2RsyDybLJ7EqtEnEsOqA", "OPEN")
        )
    )
    val downloads: StateFlow<List<DownloadItem>> = _downloads.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _previousScreen.value = _currentScreen.value
            _currentScreen.value = screen
        }
    }

    fun goBack() {
        if (_currentScreen.value != _previousScreen.value) {
            _currentScreen.value = _previousScreen.value
        } else {
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun goForward() {
        if (_currentScreen.value == AppScreen.HOME) {
            _currentScreen.value = AppScreen.URL_INPUT
        } else if (_currentScreen.value == AppScreen.URL_INPUT) {
            _currentScreen.value = AppScreen.SETTINGS
        }
    }

    fun refreshCurrentScreen() {
        viewModelScope.launch {
            _isLoadingStream.value = true
            showToast("Refreshing feed...")
            delay(800)
            _isLoadingStream.value = false
        }
    }

    fun setUrlInputText(text: String) {
        _urlInputText.value = text
    }

    fun appendUrlInputText(charStr: String) {
        _urlInputText.value = _urlInputText.value + charStr
    }

    fun backspaceUrlInputText() {
        if (_urlInputText.value.isNotEmpty()) {
            _urlInputText.value = _urlInputText.value.dropLast(1)
        }
    }

    fun clearUrlInputText() {
        _urlInputText.value = ""
    }

    fun openStreamUrl(url: String) {
        val targetUrl = if (url.isBlank()) "https://niktv.io/home" else url
        _activeStreamUrl.value = targetUrl
        _urlInputText.value = targetUrl
        viewModelScope.launch {
            _isLoadingStream.value = true
            showToast("Connecting to: $targetUrl")
            delay(600)
            _isLoadingStream.value = false
            _currentScreen.value = AppScreen.HOME
        }
    }

    fun toggleFavorite(itemId: String) {
        _recentlyViewed.update { list ->
            list.map { item ->
                if (item.id == itemId) {
                    val newFavState = !item.isFavorite
                    showToast(if (newFavState) "Added to Favorites" else "Removed from Favorites")
                    item.copy(isFavorite = newFavState)
                } else item
            }
        }
    }

    fun clearRecentlyViewed() {
        _recentlyViewed.value = emptyList()
        showToast("Recently viewed history cleared")
    }

    fun deleteDownload(downloadId: String) {
        _downloads.update { list -> list.filterNot { it.id == downloadId } }
        showToast("Download item deleted")
    }

    fun toggleExitDialog(show: Boolean) {
        _showExitDialog.value = show
    }

    fun toggleVoiceDialog(show: Boolean) {
        _showVoiceDialog.value = show
    }

    fun processVoiceCommand(command: String) {
        val lower = command.trim().lowercase()
        showToast("🎙️ Voice Command: \"$command\"")

        when {
            lower.contains("sports") -> openStreamUrl("https://niktv.io/stream/sports")
            lower.contains("news") -> openStreamUrl("https://niktv.io/stream/news")
            lower.contains("tech") || lower.contains("podcast") -> openStreamUrl("https://niktv.io/stream/tech")
            lower.contains("nature") || lower.contains("planet") -> openStreamUrl("https://niktv.io/stream/nature")
            lower.contains("home") -> navigateTo(AppScreen.HOME)
            lower.contains("setting") -> navigateTo(AppScreen.SETTINGS)
            lower.contains("download") || lower.contains("offline") -> navigateTo(AppScreen.DOWNLOADS)
            lower.contains("clear history") -> clearRecentlyViewed()
            lower.startsWith("http") || lower.startsWith("www.") || lower.contains(".com") || lower.contains(".io") || lower.contains(".m3u8") -> {
                openStreamUrl(if (lower.startsWith("http")) command else "https://$command")
            }
            else -> {
                _urlInputText.value = command
                _currentScreen.value = AppScreen.URL_INPUT
            }
        }
    }

    fun toggleFullscreen(isFullscreen: Boolean) {
        _isFullscreenStream.value = isFullscreen
        showToast(if (isFullscreen) "Entered Fullscreen Mode" else "Exited Fullscreen")
    }

    fun toggleConnectionSimulation() {
        val newStatus = !_isConnected.value
        _isConnected.value = newStatus
        if (!newStatus) {
            _currentScreen.value = AppScreen.OFFLINE_ERROR
            showToast("Network Disconnected (Simulated)")
        } else {
            showToast("Network Restored (592 Mbps)")
        }
    }

    fun updateSettings(transform: (SettingsState) -> SettingsState) {
        _settingsState.update(transform)
        showToast("Settings Updated")
    }

    fun clearCache() {
        _settingsState.update { it.copy(cacheUsedMb = 0) }
        showToast("Browser Cache Cleared (0 MB / 1024 MB)")
    }

    private fun showToast(msg: String) {
        viewModelScope.launch {
            _toastMessage.value = msg
            delay(2500)
            if (_toastMessage.value == msg) {
                _toastMessage.value = null
            }
        }
    }
}
