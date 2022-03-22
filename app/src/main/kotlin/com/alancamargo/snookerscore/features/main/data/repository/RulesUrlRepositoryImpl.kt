package com.alancamargo.snookerscore.features.main.data.repository

import com.alancamargo.snookerscore.features.main.domain.repository.RulesUrlRepository
import com.alancamargo.snookerscore.features.main.data.remote.RulesUrlRemoteDataSource

class RulesUrlRepositoryImpl(
    private val remoteDataSource: RulesUrlRemoteDataSource
) : RulesUrlRepository {

    override fun getRulesUrl(): String = remoteDataSource.getRulesUrl()

}
