package com.alancamargo.snookerscore.core.ui

import android.view.View
import androidx.annotation.StringRes

interface AdLoader {

    fun loadBannerAds(target: View, @StringRes adIdRes: Int)

}
