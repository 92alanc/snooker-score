package com.alancamargo.snookerscore.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

private const val EVENT_SCREEN_VIEWED = "SCREEN_VIEWED"

class AnalyticsImpl(private val firebaseAnalytics: FirebaseAnalytics) : Analytics {

    override fun trackScreenViewed(screenName: String) {
        track(EVENT_SCREEN_VIEWED) {
            screen(screenName)
        }
    }

    override fun track(
        eventName: String,
        propertiesBlock: EventProperties.Builder.() -> EventProperties.Builder
    ) {
        val properties = EventProperties.Builder().propertiesBlock().build()
        val map = properties.map

        firebaseAnalytics.logEvent(eventName) {
            map.forEach { (key, value) ->
                param(key, value)
            }
        }
    }

}
