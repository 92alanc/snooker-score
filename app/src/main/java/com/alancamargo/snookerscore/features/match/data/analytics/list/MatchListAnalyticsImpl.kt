package com.alancamargo.snookerscore.features.match.data.analytics.list

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val SCREEN_NAME = "match-list"
private const val BUTTON_NEW_MATCH = "new-match"
private const val CARD_MATCH = "match"

class MatchListAnalyticsImpl(private val analytics: Analytics) : MatchListAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackNewMatchClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_NEW_MATCH, screenName = SCREEN_NAME)
    }

    override fun trackMatchCardClicked() {
        analytics.trackCardClicked(cardName = CARD_MATCH, screenName = SCREEN_NAME)
    }

    override fun trackBackClicked() {
        analytics.trackBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }
}
