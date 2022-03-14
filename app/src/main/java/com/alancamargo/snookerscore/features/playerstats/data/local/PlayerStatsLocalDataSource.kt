package com.alancamargo.snookerscore.features.playerstats.data.local

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats
import kotlinx.coroutines.flow.Flow

interface PlayerStatsLocalDataSource {

    fun getPlayerStats(player: Player): Flow<PlayerStats>

    fun addOrUpdatePlayerStats(playerStats: PlayerStats): Flow<Unit>

}
