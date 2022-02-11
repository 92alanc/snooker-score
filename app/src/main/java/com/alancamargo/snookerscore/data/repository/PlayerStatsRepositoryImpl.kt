package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.PlayerStatsLocalDataSource
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
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
