package com.alancamargo.snookerscore.core.ui.ads

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

interface AdLoader {

    fun loadBannerAds(target: View, @StringRes adIdRes: Int)

    fun loadInterstitialAds(activity: AppCompatActivity, @StringRes adIdRes: Int)

}
