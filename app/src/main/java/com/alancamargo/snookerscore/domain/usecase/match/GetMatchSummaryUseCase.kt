package com.alancamargo.snookerscore.domain.usecase.match

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.MatchSummary

class GetMatchSummaryUseCase {

    operator fun invoke(frames: List<Frame>): MatchSummary {
        assert(frames.isNotEmpty()) { "There must be at least one frame" }

        val match = frames.first().match

        var player1Score = 0
        var player2Score = 0
        var player1HighestBreak = 0
        var player2HighestBreak = 0

        frames.forEach { frame ->
            if (frame.player1HighestBreak > player1HighestBreak) {
                player1HighestBreak = frame.player1HighestBreak
            }

            if (frame.player2HighestBreak > player2HighestBreak) {
                player2HighestBreak = frame.player2HighestBreak
            }

            if (frame.player1Score > frame.player2Score) {
                player1Score++
            } else {
                player2Score++
            }
        }

        val winner = if (player1Score > player2Score) {
            match.player1
        } else {
            match.player2
        }

        return MatchSummary(
            match = match,
            winner = winner,
            player1Score = player1Score,
            player2Score = player2Score,
            player1HighestBreak = player1HighestBreak,
            player2HighestBreak = player2HighestBreak
        )
    }

}
