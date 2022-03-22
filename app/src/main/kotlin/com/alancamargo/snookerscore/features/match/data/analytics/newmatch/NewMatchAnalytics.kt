package com.alancamargo.snookerscore.features.match.data.analytics.newmatch

interface NewMatchAnalytics {

    fun trackScreenViewed()

    fun trackBackClicked()

    fun trackNativeBackClicked()

    fun trackMatchStarted(numberOfFrames: Int)

    fun trackSamePlayers()

}
