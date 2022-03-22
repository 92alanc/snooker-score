package com.alancamargo.snookerscore.features.main.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

private const val SCREEN_NAME = "main"

private const val BUTTON_MATCHES = "matches"
private const val BUTTON_PLAYERS = "players"
private const val BUTTON_RULES = "rules"
private const val BUTTON_ABOUT = "about"

class MainAnalyticsImplTest {

    private val mockAnalytics = mockk<Analytics>(relaxed = true)
    private val mainAnalytics = MainAnalyticsImpl(mockAnalytics)

    @Test
    fun `trackScreenViewed should track correctly`() {
        mainAnalytics.trackScreenViewed()

        verify { mockAnalytics.trackScreenViewed(SCREEN_NAME) }
    }

    @Test
    fun `trackMatchesClicked should track correctly`() {
        mainAnalytics.trackMatchesClicked()

        verify {
            mockAnalytics.trackButtonClicked(buttonName = BUTTON_MATCHES, screenName = SCREEN_NAME)
        }
    }

    @Test
    fun `trackPlayersClicked should track correctly`() {
        mainAnalytics.trackPlayersClicked()

        verify {
            mockAnalytics.trackButtonClicked(buttonName = BUTTON_PLAYERS, screenName = SCREEN_NAME)
        }
    }

    @Test
    fun `trackRulesClicked should track correctly`() {
        mainAnalytics.trackRulesClicked()

        verify {
            mockAnalytics.trackButtonClicked(buttonName = BUTTON_RULES, screenName = SCREEN_NAME)
        }
    }

    @Test
    fun `trackAboutClicked should track correctly`() {
        mainAnalytics.trackAboutClicked()

        verify {
            mockAnalytics.trackButtonClicked(buttonName = BUTTON_ABOUT, screenName = SCREEN_NAME)
        }
    }

    @Test
    fun `trackNativeBackButtonClicked should track correctly`() {
        mainAnalytics.trackNativeBackButtonClicked()

        verify { mockAnalytics.trackNativeBackButtonClicked(SCREEN_NAME) }
    }

}
