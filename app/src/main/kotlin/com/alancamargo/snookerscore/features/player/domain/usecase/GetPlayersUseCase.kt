package com.alancamargo.snookerscore.features.player.domain.usecase

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetPlayersUseCase(private val repository: PlayerRepository) {

    operator fun invoke(): Flow<List<Player>> {
        return repository.getPlayers()
    }

}
