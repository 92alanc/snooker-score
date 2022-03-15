package com.alancamargo.snookerscore.features.player.data.analytics

interface PlayerListAnalytics {

    fun trackScreenViewed()

    fun trackNewPlayerClicked()

    fun trackSavePlayerClicked()

    fun trackPlayerCardClicked()

    fun trackPlayerCardLongClicked()

    fun trackTipDismissed()

    fun trackDoNotShowTipAgainClicked()

    fun trackBackClicked()

    fun trackNativeBackClicked()

}
