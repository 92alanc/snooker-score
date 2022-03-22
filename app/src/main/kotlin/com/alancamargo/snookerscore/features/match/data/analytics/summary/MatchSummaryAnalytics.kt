package com.alancamargo.snookerscore.features.match.data.analytics.summary

interface MatchSummaryAnalytics {

    fun trackScreenViewed()

    fun trackBackClicked()

    fun trackNativeBackClicked()

    fun trackNewMatchClicked()

}
