package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.util.UUID

class AddOrUpdatePlayerUseCase(
    private val playerRepository: PlayerRepository,
    private val playerStatsRepository: PlayerStatsRepository
) {

    operator fun invoke(player: Player): Flow<Unit> {
        val playerStats = PlayerStats(
            id = UUID.randomUUID().toString(),
            player = player,
            matchesWon = 0,
            highestScore = 0,
            highestBreak = 0
        )
        return playerRepository.addOrUpdatePlayer(player)
            .zip(playerStatsRepository.addOrUpdatePlayerStats(playerStats)) { _, _ -> }
    }

}
