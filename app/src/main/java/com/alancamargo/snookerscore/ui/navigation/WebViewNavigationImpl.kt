package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import com.alancamargo.snookerscore.ui.activities.WebViewActivity

class WebViewNavigationImpl : WebViewNavigation {

    override fun startActivity(context: Context, titleRes: Int, url: String) {
        val args = WebViewActivity.Args(titleRes = titleRes, url = url)
        val intent = WebViewActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
