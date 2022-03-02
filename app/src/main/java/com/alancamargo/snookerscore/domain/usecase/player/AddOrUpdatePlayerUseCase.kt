package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class AddOrUpdatePlayerUseCase(private val playerRepository: PlayerRepository) {

    operator fun invoke(player: Player): Flow<Unit> = playerRepository.addOrUpdatePlayer(player)

}
