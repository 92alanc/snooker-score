package com.alancamargo.snookerscore.data.local

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import kotlinx.coroutines.flow.Flow

interface PlayerStatsLocalDataSource {

    fun getPlayerStats(player: Player): Flow<PlayerStats>

    fun addOrUpdatePlayerStats(playerStats: PlayerStats): Flow<Unit>

    fun deletePlayerStats(player: Player): Flow<Unit>

}
