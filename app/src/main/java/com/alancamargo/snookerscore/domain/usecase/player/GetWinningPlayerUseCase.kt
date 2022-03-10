package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Player

private const val EXCEPTION_MESSAGE = "No winner could be determined"

class GetWinningPlayerUseCase {

    operator fun invoke(frames: List<Frame>): Player {
        if (frames.isEmpty()) {
            throw IllegalStateException(EXCEPTION_MESSAGE)
        }

        val match = frames.first().match

        if (frames.size < match.numberOfFrames) {
            throw IllegalStateException(EXCEPTION_MESSAGE)
        }

        val player1HasWon = frames.count { it.player1Score > it.player2Score } > frames.size / 2
        val player2HasWon = frames.count { it.player2Score > it.player1Score } > frames.size / 2

        val player1 = match.player1
        val player2 = match.player2

        return when {
            player1HasWon -> player1
            player2HasWon -> player2
            else -> throw IllegalStateException(EXCEPTION_MESSAGE)
        }
    }

}
