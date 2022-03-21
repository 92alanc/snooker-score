package com.alancamargo.snookerscore.features.match.data.analytics.newmatch

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val EVENT_MATCH_STARTED = "MATCH_STARTED"
private const val EVENT_SAME_PLAYERS = "SAME_PLAYERS"

private const val SCREEN_NAME = "new-match"
private const val BUTTON_START_MATCH = "start-match"
private const val PROPERTY_NUMBER_OF_FRAMES = "number-of-frames"

class NewMatchAnalyticsImpl(private val analytics: Analytics) : NewMatchAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackBackClicked() {
        analytics.trackBackButtonClicked(SCREEN_NAME)
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

    override fun trackMatchStarted(numberOfFrames: Int) {
        analytics.track(EVENT_MATCH_STARTED) {
            screen(SCREEN_NAME)
            button(BUTTON_START_MATCH)
            PROPERTY_NUMBER_OF_FRAMES withValue numberOfFrames
        }
    }

    override fun trackSamePlayers() {
        analytics.track(EVENT_SAME_PLAYERS) {
            screen(SCREEN_NAME)
            button(BUTTON_START_MATCH)
        }
    }

}
