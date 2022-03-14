package com.alancamargo.snookerscore.features.playerstats.data.local

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.playerstats.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.features.playerstats.data.mapping.toData
import com.alancamargo.snookerscore.features.playerstats.data.mapping.toDomain
import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats
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
