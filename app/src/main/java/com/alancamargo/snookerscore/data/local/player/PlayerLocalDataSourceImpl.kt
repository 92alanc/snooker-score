package com.alancamargo.snookerscore.data.local.player

import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.domain.model.Player
import kotlinx.coroutines.flow.flow

class PlayerLocalDataSourceImpl(private val playerDao: PlayerDao) : PlayerLocalDataSource {

    override fun getPlayers() = flow {
        val players = playerDao.getPlayers().map { it.toDomain() }
        emit(players)
    }

    override fun addOrUpdatePlayer(player: Player) = flow {
        val task = playerDao.addOrUpdatePlayer(player.toData())
        emit(task)
    }

    override fun deletePlayer(player: Player) = flow {
        val task = playerDao.deletePlayer(player.name)
        emit(task)
    }

}
