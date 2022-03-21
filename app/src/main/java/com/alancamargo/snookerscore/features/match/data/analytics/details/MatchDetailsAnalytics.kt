package com.alancamargo.snookerscore.features.match.data.analytics.details

interface MatchDetailsAnalytics {

    fun trackScreenViewed()

    fun trackViewSummaryClicked()

    fun trackDeleteMatchClicked()

    fun trackDeleteMatchCancelled()

    fun trackDeleteMatchConfirmed()

    fun trackBackClicked()

    fun trackNativeBackClicked()

}
