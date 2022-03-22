package com.alancamargo.snookerscore.features.match.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.match.ui.MatchSummaryActivity
import com.alancamargo.snookerscore.navigation.MatchSummaryNavigation
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame

class MatchSummaryNavigationImpl : MatchSummaryNavigation {

    override fun startActivity(context: Context, frames: List<UiFrame>) {
        val args = MatchSummaryActivity.Args(frames)
        val intent = MatchSummaryActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
