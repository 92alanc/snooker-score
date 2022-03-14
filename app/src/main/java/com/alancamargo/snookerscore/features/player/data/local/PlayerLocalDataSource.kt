package com.alancamargo.snookerscore.features.player.data.local

import com.alancamargo.snookerscore.features.player.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerLocalDataSource {

    fun getPlayers(): Flow<List<Player>>

    fun addOrUpdatePlayer(player: Player): Flow<Unit>

    fun deletePlayer(player: Player): Flow<Unit>

}
