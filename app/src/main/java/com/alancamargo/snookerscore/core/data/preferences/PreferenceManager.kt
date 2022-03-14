package com.alancamargo.snookerscore.core.data.preferences

interface PreferenceManager {

    fun shouldShowPlayerListTip(): Boolean

    fun dontShowPlayerListTipAgain()

}
