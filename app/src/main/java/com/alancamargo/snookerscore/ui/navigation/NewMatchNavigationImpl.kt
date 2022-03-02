package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.NewMatchNavigation
import com.alancamargo.snookerscore.ui.activities.NewMatchActivity

class NewMatchNavigationImpl : NewMatchNavigation {

    override fun startActivity(context: Context) {
        val intent = NewMatchActivity.getIntent(context)
        context.startActivity(intent)
    }

}
