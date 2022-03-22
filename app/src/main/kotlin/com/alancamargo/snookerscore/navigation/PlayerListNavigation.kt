package com.alancamargo.snookerscore.navigation

import android.content.Context
import android.content.Intent

interface PlayerListNavigation {

    fun getIntentToPickPlayer(context: Context): Intent

    fun startActivity(context: Context)

}
