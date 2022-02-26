package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.alancamargo.snookerscore.ui.activities.PlayerListActivity

class PlayerListNavigationImpl : PlayerListNavigation {

    override fun startActivity(context: Context) {
        val intent = PlayerListActivity.getIntent(context)
        context.startActivity(intent)
    }

}
