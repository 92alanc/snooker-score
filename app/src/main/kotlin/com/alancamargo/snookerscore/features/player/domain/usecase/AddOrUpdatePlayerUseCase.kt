package com.alancamargo.snookerscore.features.player.domain.usecase

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class AddOrUpdatePlayerUseCase(private val repository: PlayerRepository) {

    operator fun invoke(player: Player): Flow<Unit> = repository.addOrUpdatePlayer(player)

}
