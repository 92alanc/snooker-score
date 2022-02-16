package com.alancamargo.snookerscore.domain.usecase

import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import kotlinx.coroutines.flow.Flow

class AddOrUpdatePlayerStatsUseCase(private val repository: PlayerStatsRepository) {

    operator fun invoke(playerStats: PlayerStats): Flow<Unit> {
        return repository.addOrUpdatePlayerStats(playerStats)
    }

}
