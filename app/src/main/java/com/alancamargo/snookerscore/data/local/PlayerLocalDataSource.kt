package com.alancamargo.snookerscore.data.local

import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerLocalDataSource {

    fun getPlayers(): Flow<List<Player>>

    fun addPlayer(player: Player): Flow<Unit>

    fun deletePlayer(player: Player): Flow<Unit>

    fun updatePlayer(player: Player): Flow<Unit>

}
