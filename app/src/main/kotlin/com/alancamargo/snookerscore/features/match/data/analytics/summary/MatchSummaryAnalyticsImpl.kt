package com.alancamargo.snookerscore.features.match.data.analytics.summary

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val SCREEN_NAME = "match-summary"
private const val BUTTON_NEW_MATCH = "new-match"

class MatchSummaryAnalyticsImpl(private val analytics: Analytics) : MatchSummaryAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackBackClicked() {
        analytics.trackBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNewMatchClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_NEW_MATCH, screenName = SCREEN_NAME)
    }

}
