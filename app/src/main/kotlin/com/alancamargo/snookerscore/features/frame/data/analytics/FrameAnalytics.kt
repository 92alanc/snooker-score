package com.alancamargo.snookerscore.features.frame.data.analytics

interface FrameAnalytics {

    fun trackFrameStarted()

    fun trackUndoLastPottedBallClicked()

    fun trackUndoLastFoulClicked()

    fun trackEndFrameClicked()

    fun trackEndFrameCancelled()

    fun trackEndFrameConfirmed(isEndingMatch: Boolean)

    fun trackForfeitMatchClicked()

    fun trackForfeitMatchCancelled()

    fun trackForfeitMatchConfirmed()

    fun trackNativeBackClicked()

}
