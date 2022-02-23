package com.alancamargo.snookerscore.domain.usecase.playerstats

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddOrUpdatePlayerStatsUseCase(private val repository: PlayerStatsRepository) {

    operator fun invoke(
        currentPlayerStats: PlayerStats,
        frame: Frame,
        player: Player
    ): Flow<Unit> {
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

            else -> return flow { emit(Unit) }
        }

        val newPlayerStats = currentPlayerStats.copy(
            highestScore = highestScore,
            highestBreak = highestBreak
        )

        return repository.addOrUpdatePlayerStats(newPlayerStats)
    }

}
