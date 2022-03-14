package com.alancamargo.snookerscore.core.data.analytics

interface Analytics {

    fun trackScreenViewed(screenName: String)

    fun trackButtonClicked(buttonName: String, screenName: String)

    fun trackBackButtonClicked(screenName: String)

    fun trackNativeBackButtonClicked(screenName: String)

    fun track(
        eventName: String,
        propertiesBlock: EventProperties.Builder.() -> EventProperties.Builder
    )

}
