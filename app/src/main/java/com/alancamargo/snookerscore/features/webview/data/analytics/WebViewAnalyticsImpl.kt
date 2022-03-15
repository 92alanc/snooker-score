package com.alancamargo.snookerscore.features.webview.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val SCREEN_NAME = "web-view"

class WebViewAnalyticsImpl(private val analytics: Analytics) : WebViewAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackBackClicked() {
        analytics.trackBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

}
