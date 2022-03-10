package com.alancamargo.snookerscore.core.ui

import android.view.View
import com.smaato.sdk.banner.ad.BannerAdSize
import com.smaato.sdk.banner.widget.BannerView

class AdLoaderImpl : AdLoader {

    override fun loadBannerAds(target: View, adIdRes: Int) {
        (target as? BannerView)?.run {
            val adId = context.getString(adIdRes)
            loadAd(adId, BannerAdSize.XX_LARGE_320x50)
        }
    }

}
