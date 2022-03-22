package com.alancamargo.snookerscore.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

interface PlayerStatsNavigation {

    fun startActivity(context: Context, player: UiPlayer)

}
