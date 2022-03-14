package com.alancamargo.snookerscore.core.data.analytics

import com.alancamargo.snookerscore.core.data.log.Logger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

private const val EVENT_SCREEN_VIEWED = "SCREEN_VIEWED"
private const val EVENT_BUTTON_CLICKED = "BUTTON_CLICKED"

class AnalyticsImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val logger: Logger
) : Analytics {

    override fun trackScreenViewed(screenName: String) {
        track(EVENT_SCREEN_VIEWED) {
            screen(screenName)
        }
    }

    override fun trackButtonClicked(buttonName: String, screenName: String) {
        track(EVENT_BUTTON_CLICKED) {
            button(buttonName)
            screen(screenName)
        }
    }

    override fun trackBackButtonClicked(screenName: String) {
        track(EVENT_BUTTON_CLICKED) {
            screen(screenName)
            backButton()
        }
    }

    override fun trackNativeBackButtonClicked(screenName: String) {
        track(EVENT_BUTTON_CLICKED) {
            screen(screenName)
            nativeBackButton()
        }
    }

    override fun track(
        eventName: String,
        propertiesBlock: EventProperties.Builder.() -> EventProperties.Builder
    ) {
        val properties = EventProperties.Builder().propertiesBlock().build()
        val map = properties.map

        val propertiesString = map.map { "${it.key}: ${it.value}" }.joinToString()
        logger.debug("$eventName -> $propertiesString")

        firebaseAnalytics.logEvent(eventName) {
            map.forEach { (key, value) ->
                param(key, value)
            }
        }
    }

}
