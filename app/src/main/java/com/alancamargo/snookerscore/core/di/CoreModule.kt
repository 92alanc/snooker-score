package com.alancamargo.snookerscore.core.di

import android.content.Context
import androidx.room.Room
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.core.data.analytics.Analytics
import com.alancamargo.snookerscore.core.data.analytics.AnalyticsImpl
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.core.data.log.LoggerImpl
import com.alancamargo.snookerscore.core.data.preferences.PreferenceManager
import com.alancamargo.snookerscore.core.data.preferences.PreferenceManagerImpl
import com.alancamargo.snookerscore.core.data.remoteconfig.RemoteConfigManager
import com.alancamargo.snookerscore.core.data.remoteconfig.RemoteConfigManagerImpl
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.core.ui.ads.AdLoaderImpl
import com.alancamargo.snookerscore.core.data.db.DatabaseProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class CoreModule : FeatureModule() {

    override val data = module {
        factory {
            FirebaseRemoteConfig.getInstance().apply {
                setDefaultsAsync(R.xml.remote_config_defaults)
                fetchAndActivate()
            }
        }
        factory { get<Context>().getSharedPreferences("snooker-score-prefs", Context.MODE_PRIVATE) }
        factory<Analytics> {
            AnalyticsImpl(firebaseAnalytics = FirebaseAnalytics.getInstance(get()))
        }
        factory<RemoteConfigManager> { RemoteConfigManagerImpl(firebaseRemoteConfig = get()) }
        factory<Logger> { LoggerImpl() }
        factory<PreferenceManager> { PreferenceManagerImpl(sharedPreferences = get()) }
        single {
            Room.databaseBuilder(
                androidContext(),
                DatabaseProvider::class.java,
                "database"
            ).fallbackToDestructiveMigration().build()
        }
    }

    override val ui = module {
        factory<AdLoader> { AdLoaderImpl() }
    }

}
