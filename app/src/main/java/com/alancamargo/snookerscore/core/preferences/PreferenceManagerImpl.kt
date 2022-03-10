package com.alancamargo.snookerscore.core.preferences

import android.content.SharedPreferences

private const val KEY_PERSONALISED_ADS_NOTICE = "personalised-ads-notice"
private const val KEY_PLAYER_LIST_TUTORIAL = "player-list-tutorial"

class PreferenceManagerImpl(private val sharedPreferences: SharedPreferences) : PreferenceManager {

    override fun shouldShowPersonalisedAdsNotice(): Boolean {
        return sharedPreferences.getBoolean(KEY_PERSONALISED_ADS_NOTICE, true)
    }

    override fun setPersonalisedAdsNoticeAsSeen() {
        sharedPreferences.edit().putBoolean(KEY_PERSONALISED_ADS_NOTICE, false).apply()
    }

    override fun shouldShowPlayerListTutorial(): Boolean {
        return sharedPreferences.getBoolean(KEY_PLAYER_LIST_TUTORIAL, true)
    }

    override fun setPlayerListTutorialAsSeen() {
        sharedPreferences.edit().putBoolean(KEY_PLAYER_LIST_TUTORIAL, false).apply()
    }

}
