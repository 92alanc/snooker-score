package com.alancamargo.snookerscore.features.match.data.analytics.list

interface MatchListAnalytics {

    fun trackScreenViewed()

    fun trackNewMatchClicked()

    fun trackMatchCardClicked()

    fun trackBackClicked()

    fun trackNativeBackClicked()

}
