package com.alancamargo.snookerscore.data.local.match

import com.alancamargo.snookerscore.data.db.MatchDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.flow

class MatchLocalDataSourceImpl(private val database: MatchDao) : MatchLocalDataSource {

    override fun addOrUpdateMatch(match: Match) = flow {
        val task = database.addOrUpdateMatch(match.toData())
        emit(task)
    }

}
