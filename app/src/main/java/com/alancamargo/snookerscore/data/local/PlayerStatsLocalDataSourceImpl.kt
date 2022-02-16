package com.alancamargo.snookerscore.data.local

import com.alancamargo.snookerscore.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import kotlinx.coroutines.flow.flow

class PlayerStatsLocalDataSourceImpl(
    private val playerStatsDao: PlayerStatsDao
) : PlayerStatsLocalDataSource {

    override fun getPlayerStats(player: Player) = flow {
        val playerStats = playerStatsDao.getPlayerStats(player.id).toDomain(player)
        emit(playerStats)
    }

    override fun addOrUpdatePlayerStats(playerStats: PlayerStats) = flow {
        val task = playerStatsDao.addOrUpdatePlayerStats(playerStats.toData())
        emit(task)
    }

    override fun deletePlayerStats(player: Player) = flow {
        val task = playerStatsDao.deletePlayerStats(player.id)
        emit(task)
    }

}
