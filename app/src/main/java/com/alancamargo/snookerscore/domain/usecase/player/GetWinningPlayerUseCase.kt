package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Player

class GetWinningPlayerUseCase {

    operator fun invoke(frames: List<Frame>): Player? {
        if (frames.isEmpty()) {
            return null
        }

        val match = frames.first().match

        if (frames.size < match.numberOfFrames) {
            return null
        }

        val player1HasWon = frames.count { it.player1Score > it.player2Score } > frames.size / 2
        val player2HasWon = frames.count { it.player2Score > it.player1Score } > frames.size / 2

        val player1 = match.player1
        val player2 = match.player2

        return when {
            player1HasWon -> player1
            player2HasWon -> player2
            else -> null
        }
    }

}
