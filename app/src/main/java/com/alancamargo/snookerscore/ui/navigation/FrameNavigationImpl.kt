package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.FrameNavigation
import com.alancamargo.snookerscore.ui.activities.FrameActivity
import com.alancamargo.snookerscore.ui.model.UiFrame

class FrameNavigationImpl : FrameNavigation {

    override fun startActivity(context: Context, frames: List<UiFrame>) {
        val args = FrameActivity.Args(frames)
        val intent = FrameActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
