package com.alancamargo.snookerscore.data.local

import com.alancamargo.snookerscore.data.db.provider.DatabaseProvider
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.flow

class PlayerLocalDataSourceImpl(databaseProvider: DatabaseProvider) : PlayerLocalDataSource {

    private val database = databaseProvider.providePlayerDao()

    override fun getPlayers() = flow {
        val players = database.getPlayers().map { it.toDomain() }
        emit(players)
    }

    override fun addPlayer(player: Player) = flow {
        val task = database.addPlayer(player.toData())
        emit(task)
    }

    override fun deletePlayer(player: Player) = flow {
        val task = database.deletePlayer(player.toData())
        emit(task)
    }

    override fun updatePlayer(player: Player) = flow {
        val task = database.updatePlayer(player.toData())
        emit(task)
    }

}
