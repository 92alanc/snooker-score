package com.alancamargo.snookerscore.core.preferences

interface PreferenceManager {

    fun shouldShowPlayerListTip(): Boolean

    fun dontShowPlayerListTipAgain()

}
