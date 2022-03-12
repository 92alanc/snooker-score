package com.alancamargo.snookerscore.features.main.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.main.ui.MainActivity
import com.alancamargo.snookerscore.navigation.MainNavigation

class MainNavigationImpl : MainNavigation {

    override fun startActivity(context: Context) {
        val intent = MainActivity.getIntent(context)
        context.startActivity(intent)
    }

}
