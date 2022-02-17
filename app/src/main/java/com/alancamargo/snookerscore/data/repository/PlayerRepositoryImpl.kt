package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.player.PlayerLocalDataSource
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(private val localDataSource: PlayerLocalDataSource) : PlayerRepository {

    override fun getPlayers(): Flow<List<Player>> = localDataSource.getPlayers()

    override fun addOrUpdatePlayer(
        player: Player
    ): Flow<Unit> = localDataSource.addOrUpdatePlayer(player)

    override fun deletePlayer(player: Player): Flow<Unit> = localDataSource.deletePlayer(player)

    override fun hasPlayers(): Flow<Boolean> = localDataSource.hasPlayers()

}
