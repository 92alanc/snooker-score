package com.alancamargo.snookerscore.features.main.data.analytics

interface MainAnalytics {

    fun trackScreenViewed()

    fun trackMatchesClicked()

    fun trackPlayersClicked()

    fun trackRulesClicked()

    fun trackAboutClicked()

    fun trackNativeBackButtonClicked()

}
