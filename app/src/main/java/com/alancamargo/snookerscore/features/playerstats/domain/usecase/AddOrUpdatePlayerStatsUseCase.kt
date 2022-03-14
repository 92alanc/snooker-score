package com.alancamargo.snookerscore.features.playerstats.domain.usecase

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.features.playerstats.domain.repository.PlayerStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class AddOrUpdatePlayerStatsUseCase(private val repository: PlayerStatsRepository) {

    operator fun invoke(player: Player): Flow<Unit> {
        val playerStats = PlayerStats(
            id = UUID.randomUUID().toString(),
            player = player,
            matchesWon = 0,
            highestScore = 0,
            highestBreak = 0
        )

        return repository.addOrUpdatePlayerStats(playerStats)
    }

    operator fun invoke(
        currentPlayerStats: PlayerStats,
        frame: Frame,
        player: Player
    ): Flow<Unit> {
        val updatedPlayerStats = getUpdatedPlayerStats(
            currentPlayerStats,
            frame,
            player
        )

        return if (updatedPlayerStats == currentPlayerStats) {
            flow { emit(Unit) }
        } else {
            repository.addOrUpdatePlayerStats(updatedPlayerStats)
        }
    }

    private fun getUpdatedPlayerStats(
        currentPlayerStats: PlayerStats,
        frame: Frame,
        player: Player
    ): PlayerStats {
        var highestScore = currentPlayerStats.highestScore
        var highestBreak = currentPlayerStats.highestBreak

        when (player) {
            frame.match.player1 -> {
                if (frame.player1Score > highestScore) {
                    highestScore = frame.player1Score
                }

                if (frame.player1HighestBreak > highestBreak) {
                    highestBreak = frame.player1HighestBreak
                }
            }

            frame.match.player2 -> {
                if (frame.player2Score > highestScore) {
                    highestScore = frame.player2Score
                }

                if (frame.player2HighestBreak > highestBreak) {
                    highestBreak = frame.player2HighestBreak
                }
            }
        }

        return currentPlayerStats.copy(
            highestBreak = highestBreak,
            highestScore = highestScore
        )
    }

}
