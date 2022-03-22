package com.alancamargo.snookerscore.features.frame.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.frame.ui.FrameActivity
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.navigation.FrameNavigation

class FrameNavigationImpl : FrameNavigation {

    override fun startActivity(context: Context, frames: List<UiFrame>) {
        val args = FrameActivity.Args(frames)
        val intent = FrameActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
