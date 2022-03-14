package com.alancamargo.snookerscore.features.player.data.repository

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.features.player.data.local.PlayerLocalDataSource
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(private val localDataSource: PlayerLocalDataSource) : PlayerRepository {

    override fun getPlayers(): Flow<List<Player>> = localDataSource.getPlayers()

    override fun addOrUpdatePlayer(
        player: Player
    ): Flow<Unit> = localDataSource.addOrUpdatePlayer(player)

    override fun deletePlayer(player: Player): Flow<Unit> = localDataSource.deletePlayer(player)

}
