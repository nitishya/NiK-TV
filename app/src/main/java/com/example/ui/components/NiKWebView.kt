package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NiKWebView(
    url: String,
    onProgressChanged: (Int) -> Unit = {},
    onPageStarted: (String) -> Unit = {},
    onPageFinished: (String) -> Unit = {},
    onErrorReceived: (String) -> Unit = {},
    onTitleReceived: (String) -> Unit = {},
    onCustomViewShow: ((View, WebChromeClient.CustomViewCallback) -> Unit)? = null,
    onCustomViewHide: (() -> Unit)? = null,
    onDownloadStart: ((String, String, String, String, Long) -> Unit)? = null,
    modifier: Modifier = Modifier,
    onWebViewCreated: (WebView) -> Unit = {}
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    DisposableEffect(url) {
        onDispose {
            // Keep webview alive during recompositions unless explicitly torn down
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            webView.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Advanced Production WebSettings
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    builtInZoomControls = true
                    displayZoomControls = false
                    setSupportZoom(true)
                    mediaPlaybackRequiresUserGesture = false
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    cacheMode = WebSettings.LOAD_DEFAULT
                    userAgentString = "${settings.userAgentString} NiKTV-Android/1.0 TV-Mobile-Hybrid"
                }

                // Cookie Management & Persistence
                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(this, true)

                // Focusability for Android TV D-Pad navigation
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        url?.let { onPageStarted(it) }
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        url?.let { onPageFinished(it) }
                        CookieManager.getInstance().flush()
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        if (request?.isForMainFrame == true) {
                            val description = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                error?.description?.toString() ?: "Connection error"
                            } else {
                                "Network connection error"
                            }
                            onErrorReceived(description)
                        }
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val requestUrl = request?.url?.toString() ?: return false
                        if (requestUrl.startsWith("http://") || requestUrl.startsWith("https://")) {
                            return false // Handle internally in WebView
                        }
                        return try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(requestUrl))
                            context.startActivity(intent)
                            true
                        } catch (e: Exception) {
                            true
                        }
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        onProgressChanged(newProgress)
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        title?.let { onTitleReceived(it) }
                    }

                    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                        if (view != null && callback != null && onCustomViewShow != null) {
                            onCustomViewShow(view, callback)
                        } else {
                            super.onShowCustomView(view, callback)
                        }
                    }

                    override fun onHideCustomView() {
                        if (onCustomViewHide != null) {
                            onCustomViewHide()
                        } else {
                            super.onHideCustomView()
                        }
                    }

                    override fun onPermissionRequest(request: PermissionRequest?) {
                        // Grant HTML5 audio/video requests automatically or delegate
                        request?.grant(request.resources)
                    }
                }

                setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                    onDownloadStart?.invoke(url, userAgent, contentDisposition, mimetype, contentLength)
                }

                onWebViewCreated(this)
                loadUrl(url)
            }
        },
        update = { view ->
            if (view.url != url && url.isNotBlank()) {
                view.loadUrl(url)
            }
        }
    )
}
