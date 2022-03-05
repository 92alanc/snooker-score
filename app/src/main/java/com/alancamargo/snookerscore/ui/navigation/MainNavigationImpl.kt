package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.MainNavigation
import com.alancamargo.snookerscore.ui.activities.MainActivity

class MainNavigationImpl : MainNavigation {

    override fun startActivity(context: Context) {
        val intent = MainActivity.getIntent(context)
        context.startActivity(intent)
    }

}
