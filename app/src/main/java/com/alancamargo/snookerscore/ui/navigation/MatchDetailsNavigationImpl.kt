package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.MatchDetailsNavigation
import com.alancamargo.snookerscore.ui.activities.MatchDetailsActivity
import com.alancamargo.snookerscore.ui.model.UiMatch

class MatchDetailsNavigationImpl : MatchDetailsNavigation {

    override fun startActivity(context: Context, match: UiMatch) {
        val args = MatchDetailsActivity.Args(match)
        val intent = MatchDetailsActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
