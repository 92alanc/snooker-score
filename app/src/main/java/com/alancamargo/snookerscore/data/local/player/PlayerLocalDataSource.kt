package com.alancamargo.snookerscore.data.local.player

import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerLocalDataSource {

    fun getPlayers(): Flow<List<Player>>

    fun addOrUpdatePlayer(player: Player): Flow<Unit>

    fun deletePlayer(player: Player): Flow<Unit>

    fun hasPlayers(): Flow<Boolean>

}
