package com.alancamargo.snookerscore.features.match.data.local

import com.alancamargo.snookerscore.features.match.data.db.MatchDao
import com.alancamargo.snookerscore.features.match.data.mapping.toData
import com.alancamargo.snookerscore.features.match.data.mapping.toDomain
import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.player.data.db.PlayerDao
import com.alancamargo.snookerscore.features.player.data.mapping.toDomain
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
        val task = matchDao.deleteMatch(match.toData())
        emit(task)
    }

    override fun getMatches() = flow {
        val matches = matchDao.getMatches().map { dbMatch ->
            val player1 = playerDao.getPlayer(dbMatch.player1Id)?.toDomain()
                ?: throw IllegalStateException("Player 1 not found")
            val player2 = playerDao.getPlayer(dbMatch.player2Id)?.toDomain()
                ?: throw IllegalStateException("Player 2 not found")

            dbMatch.toDomain(player1, player2)
        }

        val matchesToDelete = mutableListOf<Match>()
        matches.forEach { match ->
            if (!match.isFinished) {
                matchDao.deleteMatch(match.toData())
                matchesToDelete.add(match)
            }
        }

        val result = matches.toMutableList().apply {
            removeAll(matchesToDelete)
        }
        emit(result)
    }

}
