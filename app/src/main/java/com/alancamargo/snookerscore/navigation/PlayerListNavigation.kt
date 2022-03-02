package com.alancamargo.snookerscore.navigation

import android.content.Context

interface PlayerListNavigation {

    fun startActivity(context: Context, isPickingPlayer: Boolean = false)

}
