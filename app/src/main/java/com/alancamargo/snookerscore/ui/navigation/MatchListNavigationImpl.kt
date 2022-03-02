package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.MatchListNavigation
import com.alancamargo.snookerscore.ui.activities.MatchListActivity

class MatchListNavigationImpl : MatchListNavigation {

    override fun startActivity(context: Context) {
        val intent = MatchListActivity.getIntent(context)
        context.startActivity(intent)
    }

}
