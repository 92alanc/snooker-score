package com.alancamargo.snookerscore.data.local.playerstats

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
        val playerStats = playerStatsDao.getPlayerStats(player.name).toDomain(player)
        emit(playerStats)
    }

    override fun addOrUpdatePlayerStats(playerStats: PlayerStats) = flow {
        val task = playerStatsDao.addOrUpdatePlayerStats(playerStats.toData())
        emit(task)
    }

}
