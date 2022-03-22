package com.alancamargo.snookerscore.features.match.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.match.ui.NewMatchActivity
import com.alancamargo.snookerscore.navigation.NewMatchNavigation

class NewMatchNavigationImpl : NewMatchNavigation {

    override fun startActivity(context: Context) {
        val intent = NewMatchActivity.getIntent(context)
        context.startActivity(intent)
    }

}
