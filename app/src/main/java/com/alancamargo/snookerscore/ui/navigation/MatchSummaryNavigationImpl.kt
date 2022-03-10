package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.MatchSummaryNavigation
import com.alancamargo.snookerscore.ui.activities.MatchSummaryActivity
import com.alancamargo.snookerscore.ui.model.UiFrame

class MatchSummaryNavigationImpl : MatchSummaryNavigation {

    override fun startActivity(context: Context, frames: List<UiFrame>) {
        val args = MatchSummaryActivity.Args(frames)
        val intent = MatchSummaryActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
