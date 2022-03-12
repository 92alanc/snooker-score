package com.alancamargo.snookerscore.core.di

import android.content.Context
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.analytics.Analytics
import com.alancamargo.snookerscore.core.analytics.AnalyticsImpl
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.core.log.LoggerImpl
import com.alancamargo.snookerscore.core.preferences.PreferenceManager
import com.alancamargo.snookerscore.core.preferences.PreferenceManagerImpl
import com.alancamargo.snookerscore.core.remoteconfig.RemoteConfigManager
import com.alancamargo.snookerscore.core.remoteconfig.RemoteConfigManagerImpl
import com.alancamargo.snookerscore.core.ui.AdLoader
import com.alancamargo.snookerscore.core.ui.AdLoaderImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.dsl.module

val coreModule = module {
    factory {
        FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate()
        }
    }
    factory<RemoteConfigManager> { RemoteConfigManagerImpl(firebaseRemoteConfig = get()) }
    factory<Logger> { LoggerImpl() }
    factory<PreferenceManager> { PreferenceManagerImpl(sharedPreferences = get()) }
    factory { get<Context>().getSharedPreferences("snooker-score-prefs", Context.MODE_PRIVATE) }
    factory<AdLoader> { AdLoaderImpl() }
    factory<Analytics> { AnalyticsImpl(firebaseAnalytics = FirebaseAnalytics.getInstance(get())) }
}
