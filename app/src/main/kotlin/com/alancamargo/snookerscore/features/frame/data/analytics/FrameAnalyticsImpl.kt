package com.alancamargo.snookerscore.features.frame.data.analytics

import com.alancamargo.snookerscore.core.data.analytics.Analytics

private const val EVENT_MATCH_ENDED = "MATCH_ENDED"

private const val BUTTON_UNDO_LAST_POTTED_BALL = "undo-last-potted-ball"
private const val BUTTON_UNDO_LAST_FOUL = "undo-last-foul"
private const val BUTTON_END_FRAME = "end-frame"
private const val BUTTON_CANCEL_END_FRAME = "cancel-end-frame"
private const val BUTTON_CONFIRM_END_FRAME = "confirm-end-frame"
private const val BUTTON_FORFEIT_MATCH = "forfeit_match"
private const val BUTTON_CANCEL_FORFEIT_MATCH = "cancel-forfeit-match"
private const val BUTTON_CONFIRM_FORFEIT_MATCH = "confirm-forfeit-match"
private const val SCREEN_NAME = "frame"

class FrameAnalyticsImpl(private val analytics: Analytics) : FrameAnalytics {

    override fun trackFrameStarted() {
        analytics.trackScreenViewed(SCREEN_NAME)
    }

    override fun trackUndoLastPottedBallClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_UNDO_LAST_POTTED_BALL,
            screenName = SCREEN_NAME
        )
    }

    override fun trackUndoLastFoulClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_UNDO_LAST_FOUL,
            screenName = SCREEN_NAME
        )
    }

    override fun trackEndFrameClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_END_FRAME,
            screenName = SCREEN_NAME
        )
    }

    override fun trackEndFrameCancelled() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CANCEL_END_FRAME,
            screenName = SCREEN_NAME
        )
    }

    override fun trackEndFrameConfirmed(isEndingMatch: Boolean) {
        if (isEndingMatch) {
            analytics.track(EVENT_MATCH_ENDED) {
                screen(SCREEN_NAME)
            }
        } else {
            analytics.trackButtonClicked(
                buttonName = BUTTON_CONFIRM_END_FRAME,
                screenName = SCREEN_NAME
            )
        }
    }

    override fun trackForfeitMatchClicked() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_FORFEIT_MATCH,
            screenName = SCREEN_NAME
        )
    }

    override fun trackForfeitMatchCancelled() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CANCEL_FORFEIT_MATCH,
            screenName = SCREEN_NAME
        )
    }

    override fun trackForfeitMatchConfirmed() {
        analytics.trackButtonClicked(
            buttonName = BUTTON_CONFIRM_FORFEIT_MATCH,
            screenName = SCREEN_NAME
        )
    }

    override fun trackNativeBackClicked() {
        analytics.trackNativeBackButtonClicked(SCREEN_NAME)
    }

}
