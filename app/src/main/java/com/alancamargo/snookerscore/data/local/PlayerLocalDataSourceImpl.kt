package com.alancamargo.snookerscore.data.local

import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.flow

class PlayerLocalDataSourceImpl(
    private val playerDao: PlayerDao,
    private val playerStatsDao: PlayerStatsDao
) : PlayerLocalDataSource {

    override fun getPlayers() = flow {
        val players = playerDao.getPlayers().map { it.toDomain() }
        emit(players)
    }

    override fun addOrUpdatePlayer(player: Player) = flow {
        val task = playerDao.addOrUpdatePlayer(player.toData())
        emit(task)
    }

    override fun deletePlayer(player: Player) = flow {
        val playerId = player.id

        val task = run {
            playerDao.deletePlayer(playerId)
            playerStatsDao.deletePlayerStats(playerId)
        }

        emit(task)
    }

}
