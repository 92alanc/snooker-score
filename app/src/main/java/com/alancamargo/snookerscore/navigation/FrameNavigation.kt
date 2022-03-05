package com.alancamargo.snookerscore.navigation

import android.content.Context
import com.alancamargo.snookerscore.ui.model.UiFrame

interface FrameNavigation {

    fun startActivity(context: Context, frames: List<UiFrame>)

}
