package com.alancamargo.snookerscore.domain.repository

import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayers(): Flow<List<Player>>

    fun addOrUpdatePlayer(player: Player): Flow<Unit>

    fun deletePlayer(player: Player): Flow<Unit>

    fun hasPlayers(): Flow<Boolean>

}
