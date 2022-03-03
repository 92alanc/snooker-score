package com.alancamargo.snookerscore.data.local.match

import com.alancamargo.snookerscore.data.db.MatchDao
import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Match
import kotlinx.coroutines.flow.flow

class MatchLocalDataSourceImpl(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao
) : MatchLocalDataSource {

    override fun addMatch(match: Match) = flow {
        val task = matchDao.addMatch(match.toData())
        emit(task)
    }

    override fun deleteMatch(match: Match) = flow {
        val task = matchDao.deleteMatch(match.dateTime)
        emit(task)
    }

    override fun getMatches() = flow {
        val matches = matchDao.getMatches().map { dbMatch ->
            val player1 = playerDao.getPlayer(dbMatch.player1Id).toDomain()
            val player2 = playerDao.getPlayer(dbMatch.player2Id).toDomain()

            dbMatch.toDomain(player1, player2)
        }

        emit(matches)
    }

}
