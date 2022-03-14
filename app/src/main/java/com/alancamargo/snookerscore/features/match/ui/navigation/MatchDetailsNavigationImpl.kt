package com.alancamargo.snookerscore.features.match.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.match.ui.MatchDetailsActivity
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch
import com.alancamargo.snookerscore.navigation.MatchDetailsNavigation

class MatchDetailsNavigationImpl : MatchDetailsNavigation {

    override fun startActivity(context: Context, match: UiMatch) {
        val args = MatchDetailsActivity.Args(match)
        val intent = MatchDetailsActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
