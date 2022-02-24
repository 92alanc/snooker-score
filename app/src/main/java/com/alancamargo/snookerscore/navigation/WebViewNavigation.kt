package com.alancamargo.snookerscore.navigation

import android.content.Context

interface WebViewNavigation {

    fun startActivity(context: Context, titleRes: Int, url: String)

}
