package com.alancamargo.snookerscore.features.match.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.match.ui.MatchListActivity
import com.alancamargo.snookerscore.navigation.MatchListNavigation

class MatchListNavigationImpl : MatchListNavigation {

    override fun startActivity(context: Context) {
        val intent = MatchListActivity.getIntent(context)
        context.startActivity(intent)
    }

}
