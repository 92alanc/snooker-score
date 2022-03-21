package com.alancamargo.snookerscore.features.match.data.analytics.details

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val SCREEN_NAME = "match-details"
private const val BUTTON_VIEW_SUMMARY = "view-summary"
private const val BUTTON_DELETE_MATCH = "delete-match"
private const val BUTTON_CANCEL_DELETE_MATCH = "cancel-delete-match"
private const val BUTTON_CONFIRM_DELETE_MATCH = "confirm-delete-match"

class MatchDetailsAnalyticsImpl(private val analytics: Analytics) : MatchDetailsAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackViewSummaryClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_VIEW_SUMMARY,
            screenName = SCREEN_NAME
        )
    }

    override fun trackDeleteMatchClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_DELETE_MATCH,
            screenName = SCREEN_NAME
        )
    }

    override fun trackDeleteMatchCancelled() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CANCEL_DELETE_MATCH,
            screenName = SCREEN_NAME
        )
    }

    override fun trackDeleteMatchConfirmed() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CONFIRM_DELETE_MATCH,
            screenName = SCREEN_NAME
        )
    }

    override fun trackBackClicked() {
        analytics.trackBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

}
