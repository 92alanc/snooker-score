package com.alancamargo.snookerscore.core.preferences

import android.content.SharedPreferences

private const val KEY_PLAYER_LIST_TIP = "player-list-tip"

class PreferenceManagerImpl(private val sharedPreferences: SharedPreferences) : PreferenceManager {

    override fun shouldShowPlayerListTip(): Boolean {
        return sharedPreferences.getBoolean(KEY_PLAYER_LIST_TIP, true)
    }

    override fun dontShowPlayerListTipAgain() {
        sharedPreferences.edit().putBoolean(KEY_PLAYER_LIST_TIP, false).apply()
    }

}
