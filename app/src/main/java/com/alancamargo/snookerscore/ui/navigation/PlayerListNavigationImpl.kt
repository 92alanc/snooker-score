package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.alancamargo.snookerscore.ui.activities.PlayerListActivity

class PlayerListNavigationImpl : PlayerListNavigation {

    override fun startActivity(context: Context, isPickingPlayer: Boolean) {
        val args = PlayerListActivity.Args(isPickingPlayer)
        val intent = PlayerListActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
