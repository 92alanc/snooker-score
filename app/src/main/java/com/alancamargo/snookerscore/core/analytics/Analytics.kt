package com.alancamargo.snookerscore.core.analytics

interface Analytics {

    fun trackScreenViewed(screenName: String)

    fun track(
        eventName: String,
        propertiesBlock: EventProperties.Builder.() -> EventProperties.Builder
    )

}
