package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class HasPlayersUseCase(private val repository: PlayerRepository) {

    operator fun invoke(): Flow<Boolean> = repository.hasPlayers()

}
