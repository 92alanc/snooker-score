package com.alancamargo.snookerscore.core.data.analytics

private const val PROPERTY_SCREEN_NAME = "screen-name"
private const val PROPERTY_BUTTON_NAME = "button-name"

const val BUTTON_BACK = "back"
const val BUTTON_BACK_NATIVE = "back-native"

class EventProperties private constructor(val map: Map<String, String>) {

    class Builder {

        private val map = mutableMapOf<String, String>()

        fun screen(name: String): Builder = PROPERTY_SCREEN_NAME withValue name

        fun backButton(): Builder = button(BUTTON_BACK)

        fun nativeBackButton(): Builder = button(BUTTON_BACK_NATIVE)

        fun button(name: String): Builder = PROPERTY_BUTTON_NAME withValue name

        infix fun String.withValue(value: Any): Builder {
            map[this] = value.toString()
            return this@Builder
        }

        fun build() = EventProperties(map)

    }

}
