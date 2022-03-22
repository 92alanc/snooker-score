package com.alancamargo.snookerscore.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame

interface MatchSummaryNavigation {

    fun startActivity(context: Context, frames: List<UiFrame>)

}
