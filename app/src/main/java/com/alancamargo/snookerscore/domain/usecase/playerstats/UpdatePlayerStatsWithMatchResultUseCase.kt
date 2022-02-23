package com.alancamargo.snookerscore.domain.usecase.playerstats

import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import kotlinx.coroutines.flow.Flow

class UpdatePlayerStatsWithMatchResultUseCase(private val repository: PlayerStatsRepository) {

    operator fun invoke(winnerCurrentStats: PlayerStats): Flow<Unit> {
        val updatedStats = winnerCurrentStats.copy(matchesWon = winnerCurrentStats.matchesWon + 1)
        return repository.addOrUpdatePlayerStats(updatedStats)
    }

}
