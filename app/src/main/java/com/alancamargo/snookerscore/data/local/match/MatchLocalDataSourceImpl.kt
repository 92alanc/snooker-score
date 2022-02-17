package com.alancamargo.snookerscore.data.local.match

import com.alancamargo.snookerscore.data.db.MatchDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.flow

class MatchLocalDataSourceImpl(private val matchDao: MatchDao) : MatchLocalDataSource {

    override fun addOrUpdateMatch(match: Match) = flow {
        val task = matchDao.addOrUpdateMatch(match.toData())
        emit(task)
    }

    override fun deleteMatch(match: Match) = flow {
        val task = matchDao.deleteMatch(match.dateTime)
        emit(task)
    }

}
