package com.alancamargo.snookerscore.features.player.domain.repository

import com.alancamargo.snookerscore.features.player.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayers(): Flow<List<Player>>

    fun addOrUpdatePlayer(player: Player): Flow<Unit>

    fun deletePlayer(player: Player): Flow<Unit>

}
