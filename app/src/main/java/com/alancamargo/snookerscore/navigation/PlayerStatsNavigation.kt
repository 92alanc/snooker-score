package com.alancamargo.snookerscore.navigation

import android.content.Context
import com.alancamargo.snookerscore.ui.model.UiPlayer

interface PlayerStatsNavigation {

    fun startActivity(context: Context, player: UiPlayer)

}
