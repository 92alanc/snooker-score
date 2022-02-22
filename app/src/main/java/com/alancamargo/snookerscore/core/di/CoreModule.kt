package com.alancamargo.snookerscore.core.di

import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.remoteconfig.RemoteConfigManager
import com.alancamargo.snookerscore.core.remoteconfig.RemoteConfigManagerImpl
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
}
