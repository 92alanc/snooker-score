package com.alancamargo.snookerscore.features.webview.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

private const val SCREEN_NAME = "web-view"

class WebViewAnalyticsImplTest {

    private val mockAnalytics = mockk<Analytics>(relaxed = true)
    private val webViewAnalytics = WebViewAnalyticsImpl(mockAnalytics)

    @Test
    fun `trackScreenViewed should track correctly`() {
        webViewAnalytics.trackScreenViewed()

        verify { mockAnalytics.trackScreenViewed(SCREEN_NAME) }
    }

    @Test
    fun `trackBackClicked should track correctly`() {
        webViewAnalytics.trackBackClicked()

        verify { mockAnalytics.trackBackButtonClicked(SCREEN_NAME) }
    }

    @Test
    fun `trackNativeBackClicked should track correctly`() {
        webViewAnalytics.trackNativeBackClicked()

        verify { mockAnalytics.trackNativeBackButtonClicked(SCREEN_NAME) }
    }

}
