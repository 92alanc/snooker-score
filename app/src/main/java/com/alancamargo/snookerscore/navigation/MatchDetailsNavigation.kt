package com.alancamargo.snookerscore.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch

interface MatchDetailsNavigation {

    fun startActivity(context: Context, match: UiMatch)

}
