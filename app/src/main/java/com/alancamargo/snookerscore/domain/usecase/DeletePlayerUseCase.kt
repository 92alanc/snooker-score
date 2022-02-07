package com.alancamargo.snookerscore.domain.usecase

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class DeletePlayerUseCase(private val repository: PlayerRepository) {

    operator fun invoke(player: Player): Flow<Unit> {
        return repository.deletePlayer(player)
    }

}
