package com.alancamargo.snookerscore.core.data.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfigManagerImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigManager {

    override fun getString(key: String): String {
        return firebaseRemoteConfig.getString(key)
    }

}
