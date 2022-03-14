package com.alancamargo.snookerscore.features.main.data.remote

import com.alancamargo.snookerscore.core.data.remoteconfig.RemoteConfigManager

private const val REMOTE_CONFIG_KEY = "rules_url"

class RulesUrlRemoteDataSourceImpl(
    private val remoteConfigManager: RemoteConfigManager
) : RulesUrlRemoteDataSource {

    override fun getRulesUrl(): String = remoteConfigManager.getString(REMOTE_CONFIG_KEY)

}
