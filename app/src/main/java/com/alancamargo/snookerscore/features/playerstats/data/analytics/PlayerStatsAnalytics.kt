package com.alancamargo.snookerscore.features.playerstats.data.analytics

interface PlayerStatsAnalytics {

    fun trackScreenViewed()

    fun trackBackClicked()

    fun trackNativeBackClicked()

    fun trackDeletePlayerClicked()

    fun trackDeletePlayerCancelled()

    fun trackDeletePlayerConfirmed()

}
