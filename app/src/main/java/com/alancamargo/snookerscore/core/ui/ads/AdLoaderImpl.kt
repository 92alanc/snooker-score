package com.alancamargo.snookerscore.core.ui.ads

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alancamargo.snookerscore.R
import com.smaato.sdk.banner.ad.BannerAdSize
import com.smaato.sdk.banner.widget.BannerView
import com.smaato.sdk.interstitial.EventListener
import com.smaato.sdk.interstitial.Interstitial
import com.smaato.sdk.interstitial.InterstitialAd
import com.smaato.sdk.interstitial.InterstitialError
import com.smaato.sdk.interstitial.InterstitialRequestError

class AdLoaderImpl : AdLoader {

    override fun loadBannerAds(target: View, adIdRes: Int) {
        (target as? BannerView)?.run {
            val adId = context.getString(adIdRes)
            loadAd(adId, BannerAdSize.XX_LARGE_320x50)
        }
    }

    override fun loadInterstitialAds(activity: AppCompatActivity, adIdRes: Int) {
        val eventListener = getEventListener(activity)
        Interstitial.loadAd(activity.getString(adIdRes), eventListener)
    }

    private fun getEventListener(activity: AppCompatActivity) = object : EventListener {
        override fun onAdLoaded(interstitialAd: InterstitialAd) = with(interstitialAd) {
            val colour = ContextCompat.getColor(activity, R.color.black)
            setBackgroundColor(colour)
            showAd(activity)
        }

        override fun onAdFailedToLoad(error: InterstitialRequestError) {
            // Do nothing
        }

        override fun onAdError(interstitialAd: InterstitialAd, error: InterstitialError) {
            // Do nothing
        }

        override fun onAdOpened(interstitialAd: InterstitialAd) {
            // Do nothing
        }

        override fun onAdClosed(interstitialAd: InterstitialAd) {
            // Do nothing
        }

        override fun onAdClicked(interstitialAd: InterstitialAd) {
            // Do nothing
        }

        override fun onAdImpression(interstitialAd: InterstitialAd) {
            // Do nothing
        }

        override fun onAdTTLExpired(interstitialAd: InterstitialAd) {
            // Do nothing
        }
    }

}
