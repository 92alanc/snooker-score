package com.alancamargo.snookerscore.core.preferences

interface PreferenceManager {

    fun shouldShowPersonalisedAdsNotice(): Boolean

    fun setPersonalisedAdsNoticeAsSeen()

    fun shouldShowPlayerListTutorial(): Boolean

    fun setPlayerListTutorialAsSeen()

}
