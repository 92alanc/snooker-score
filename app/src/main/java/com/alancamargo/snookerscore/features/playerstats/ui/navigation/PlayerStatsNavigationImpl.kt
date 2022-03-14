package com.alancamargo.snookerscore.features.playerstats.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.features.playerstats.ui.PlayerStatsActivity
import com.alancamargo.snookerscore.navigation.PlayerStatsNavigation

class PlayerStatsNavigationImpl : PlayerStatsNavigation {

    override fun startActivity(context: Context, player: UiPlayer) {
        val args = PlayerStatsActivity.Args(player)
        val intent = PlayerStatsActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
