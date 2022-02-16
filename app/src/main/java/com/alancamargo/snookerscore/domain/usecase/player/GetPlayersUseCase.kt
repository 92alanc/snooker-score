package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlayersUseCase(private val repository: PlayerRepository) {

    operator fun invoke(): Flow<List<Player>> {
        return repository.getPlayers()
    }

}
