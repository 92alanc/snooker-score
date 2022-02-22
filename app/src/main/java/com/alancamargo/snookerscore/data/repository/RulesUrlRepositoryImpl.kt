package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.remote.RulesUrlRemoteDataSource
import com.alancamargo.snookerscore.domain.repository.RulesUrlRepository

class RulesUrlRepositoryImpl(
    private val remoteDataSource: RulesUrlRemoteDataSource
) : RulesUrlRepository {

    override fun getRulesUrl(): String = remoteDataSource.getRulesUrl()

}
