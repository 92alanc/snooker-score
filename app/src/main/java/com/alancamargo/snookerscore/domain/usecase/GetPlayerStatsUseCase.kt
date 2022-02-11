package com.alancamargo.snookerscore.domain.usecase

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import kotlinx.coroutines.flow.Flow

class GetPlayerStatsUseCase(private val repository: PlayerStatsRepository) {

    operator fun invoke(player: Player): Flow<PlayerStats> {
        return repository.getPlayerStats(player)
    }

}
