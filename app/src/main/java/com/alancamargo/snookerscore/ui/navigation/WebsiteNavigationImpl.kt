package com.alancamargo.snookerscore.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.alancamargo.snookerscore.navigation.WebsiteNavigation

class WebsiteNavigationImpl : WebsiteNavigation {

    override fun openWebsite(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

}
