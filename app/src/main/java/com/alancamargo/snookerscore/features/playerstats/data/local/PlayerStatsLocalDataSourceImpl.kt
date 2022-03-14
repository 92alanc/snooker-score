package com.alancamargo.snookerscore.features.playerstats.data.local

import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.features.playerstats.data.db.PlayerStatsDao
import kotlinx.coroutines.flow.flow

class PlayerStatsLocalDataSourceImpl(
    private val playerStatsDao: PlayerStatsDao
) : PlayerStatsLocalDataSource {

    override fun getPlayerStats(player: Player) = flow {
        val playerStats = playerStatsDao.getPlayerStats(player.id)?.toDomain(player)
            ?: throw IllegalStateException("Player stats not found")

        emit(playerStats)
    }

    override fun addOrUpdatePlayerStats(playerStats: PlayerStats) = flow {
        val playerStatsExist = playerStatsDao.getPlayerStats(playerStats.player.id) != null

        val task = if (playerStatsExist) {
            playerStatsDao.updatePlayerStats(playerStats.toData())
        } else {
            playerStatsDao.addPlayerStats(playerStats.toData())
        }

        emit(task)
    }

}
