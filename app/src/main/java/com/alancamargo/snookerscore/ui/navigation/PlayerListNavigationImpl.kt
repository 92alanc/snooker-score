package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import android.content.Intent
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.alancamargo.snookerscore.ui.activities.PlayerListActivity

class PlayerListNavigationImpl : PlayerListNavigation {

    override fun getIntentToPickPlayer(context: Context): Intent {
        val args = PlayerListActivity.Args(isPickingPlayer = true)
        return PlayerListActivity.getIntent(context, args)
    }

    override fun startActivity(context: Context) {
        val args = PlayerListActivity.Args(isPickingPlayer = false)
        val intent = PlayerListActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
