package com.alancamargo.snookerscore.features.playerstats.data.repository

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats
import com.alancamargo.snookerscore.features.playerstats.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.features.playerstats.data.local.PlayerStatsLocalDataSource
import kotlinx.coroutines.flow.Flow

class PlayerStatsRepositoryImpl(
    private val localDataSource: PlayerStatsLocalDataSource
) : PlayerStatsRepository {

    override fun getPlayerStats(player: Player): Flow<PlayerStats> {
        return localDataSource.getPlayerStats(player)
    }

    override fun addOrUpdatePlayerStats(playerStats: PlayerStats): Flow<Unit> {
        return localDataSource.addOrUpdatePlayerStats(playerStats)
    }

}
