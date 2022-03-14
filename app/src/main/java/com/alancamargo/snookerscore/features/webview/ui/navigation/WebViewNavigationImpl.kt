package com.alancamargo.snookerscore.features.webview.ui.navigation

import android.content.Context
import com.alancamargo.snookerscore.features.webview.ui.WebViewActivity
import com.alancamargo.snookerscore.navigation.WebViewNavigation

class WebViewNavigationImpl : WebViewNavigation {

    override fun startActivity(context: Context, titleRes: Int, url: String) {
        val args = WebViewActivity.Args(titleRes = titleRes, url = url)
        val intent = WebViewActivity.getIntent(context, args)
        context.startActivity(intent)
    }

}
