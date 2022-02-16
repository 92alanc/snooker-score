package com.alancamargo.snookerscore.data.local

import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

class PlayerLocalDataSourceImpl(
    private val playerDao: PlayerDao,
    private val playerStatsLocalDataSource: PlayerStatsLocalDataSource
) : PlayerLocalDataSource {

    override fun getPlayers() = flow {
        val players = playerDao.getPlayers().map { it.toDomain() }
        emit(players)
    }

    override fun addOrUpdatePlayer(player: Player) = flow {
        val task = playerDao.addOrUpdatePlayer(player.toData())
        emit(task)
    }

    override fun deletePlayer(player: Player): Flow<Unit> {
        val flow = flow {
            val task = playerDao.deletePlayer(player.id)
            emit(task)
        }

        return playerStatsLocalDataSource.deletePlayerStats(player).zip(flow) { _, _ -> }
    }

}
