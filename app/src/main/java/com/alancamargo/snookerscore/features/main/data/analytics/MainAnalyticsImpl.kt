package com.alancamargo.snookerscore.features.main.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val SCREEN_NAME = "main"

private const val BUTTON_MATCHES = "matches"
private const val BUTTON_PLAYERS = "players"
private const val BUTTON_RULES = "rules"
private const val BUTTON_ABOUT = "about"

class MainAnalyticsImpl(private val analytics: Analytics) : MainAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackMatchesClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_MATCHES, screenName = SCREEN_NAME)
    }

    override fun trackPlayersClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_PLAYERS, screenName = SCREEN_NAME)
    }

    override fun trackRulesClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_RULES, screenName = SCREEN_NAME)
    }

    override fun trackAboutClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_ABOUT, screenName = SCREEN_NAME)
    }

    override fun trackNativeBackButtonClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

}
