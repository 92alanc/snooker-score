package com.alancamargo.snookerscore.features.player.data.local

import com.alancamargo.snookerscore.features.player.data.db.PlayerDao
import com.alancamargo.snookerscore.features.player.data.mapping.toData
import com.alancamargo.snookerscore.features.player.data.mapping.toDomain
import com.alancamargo.snookerscore.features.player.domain.model.Player
import kotlinx.coroutines.flow.flow

class PlayerLocalDataSourceImpl(private val playerDao: PlayerDao) : PlayerLocalDataSource {

    override fun getPlayers() = flow {
        val players = playerDao.getPlayers().map { it.toDomain() }
        emit(players)
    }

    override fun addOrUpdatePlayer(player: Player) = flow {
        val playerExists = playerDao.getPlayer(player.id) != null

        val task = if (playerExists) {
            playerDao.updatePlayer(player.toData())
        } else {
            playerDao.addPlayer(player.toData())
        }

        emit(task)
    }

    override fun deletePlayer(player: Player) = flow {
        val task = playerDao.deletePlayer(player.toData())
        emit(task)
    }

}
