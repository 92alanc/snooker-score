package com.alancamargo.snookerscore.features.player.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val EVENT_CARD_LONG_CLICKED = "CARD_LONG_CLICKED"
private const val EVENT_DIALOGUE_DISMISSED = "DIALOGUE_DISMISSED"

private const val PROPERTY_DIALOGUE_NAME = "dialogue-name"

private const val SCREEN_NAME = "player-list"
private const val BUTTON_NEW_PLAYER = "new-player"
private const val CARD_NAME = "player"
private const val DIALOGUE_NAME = "tip"
private const val BUTTON_DO_NOT_SHOW_TIP_AGAIN = "do-not-show-tip-again"
private const val BUTTON_SAVE_PLAYER = "save-player"

class PlayerListAnalyticsImpl(private val analytics: Analytics) : PlayerListAnalytics {

    override fun trackScreenViewed() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackNewPlayerClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_NEW_PLAYER, screenName = SCREEN_NAME)
    }

    override fun trackSavePlayerClicked() {
        analytics.trackButtonClicked(buttonName = BUTTON_SAVE_PLAYER, screenName = SCREEN_NAME)
    }

    override fun trackPlayerCardClicked() {
        analytics.trackCardClicked(cardName = CARD_NAME, screenName = SCREEN_NAME)
    }

    override fun trackPlayerCardLongClicked() {
        analytics.track(EVENT_CARD_LONG_CLICKED) {
            card(CARD_NAME)
            screen(SCREEN_NAME)
        }
    }

    override fun trackTipDismissed() {
        analytics.track(EVENT_DIALOGUE_DISMISSED) {
            screen(SCREEN_NAME)
            PROPERTY_DIALOGUE_NAME withValue DIALOGUE_NAME
        }
    }

    override fun trackDoNotShowTipAgainClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_DO_NOT_SHOW_TIP_AGAIN,
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
