package com.alancamargo.snookerscore.features.playerstats.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val SCREEN_NAME = "player-stats"
private const val BUTTON_DELETE_PLAYER = "delete-player"
private const val BUTTON_CANCEL_DELETE_PLAYER = "cancel-delete-player"
private const val BUTTON_CONFIRM_DELETE_PLAYER = "confirm-delete-player"

class PlayerStatsAnalyticsImpl(private val analytics: Analytics) : PlayerStatsAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackBackClicked() {
        analytics.trackBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

    override fun trackDeletePlayerClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_DELETE_PLAYER, screenName = SCREEN_NAME)
    }

    override fun trackDeletePlayerCancelled() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CANCEL_DELETE_PLAYER,
            screenName = SCREEN_NAME
        )
    }

    override fun trackDeletePlayerConfirmed() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CONFIRM_DELETE_PLAYER,
            screenName = SCREEN_NAME
        )
    }

}
