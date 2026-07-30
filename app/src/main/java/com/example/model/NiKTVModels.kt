package com.example.model

enum class AppScreen(val label: String) {
    HOME("Home"),
    URL_INPUT("Enter URL"),
    SETTINGS("Settings"),
    DOWNLOADS("Downloads"),
    OFFLINE_ERROR("Network Status")
}

data class RecentlyViewedItem(
    val id: String,
    val title: String,
    val category: String,
    val imageUrl: String,
    val streamUrl: String,
    val isFavorite: Boolean = false,
    val durationOrTag: String = "LIVE"
)

data class FavoriteChannel(
    val id: String,
    val title: String,
    val category: String,
    val badge: String,
    val imageUrl: String,
    val streamUrl: String
)

data class DownloadItem(
    val id: String,
    val title: String,
    val sizeText: String,
    val fileType: DownloadType,
    val imageUrl: String = "",
    val actionLabel: String = "OPEN"
)

enum class DownloadType {
    VIDEO, APK, DOCUMENT, AUDIO
}

data class SettingsState(
    val defaultHomeUrl: String = "https://niktv.io/home",
    val autoUpdateClient: Boolean = true,
    val enableJavaScript: Boolean = true,
    val acceptThirdPartyCookies: Boolean = false,
    val downloadLocation: String = "/internal_storage/downloads/niktv",
    val activeTheme: String = "Midnight Slate",
    val fontScale: Float = 1.1f,
    val cacheUsedMb: Int = 428,
    val cacheTotalMb: Int = 1024
)
