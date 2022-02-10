package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.local.PlayerLocalDataSource
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class PlayerRepositoryImpl(private val localDataSource: PlayerLocalDataSource) : PlayerRepository {

    override fun getPlayers(): Flow<List<Player>> = localDataSource.getPlayers()

    override fun addPlayer(player: Player): Flow<Unit> = localDataSource.addPlayer(player)

    override fun updatePlayer(player: Player): Flow<Unit> = localDataSource.updatePlayer(player)

    override fun deletePlayer(player: Player): Flow<Unit> = localDataSource.deletePlayer(player)

}
