package com.alancamargo.snookerscore.features.match.ui.mapping

import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.domain.model.MatchSummary
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch
import com.alancamargo.snookerscore.features.match.ui.model.UiMatchSummary

fun Match.toUi() = UiMatch(
    dateTime = dateTime,
    player1 = player1.toUi(),
    player2 = player2.toUi(),
    numberOfFrames = numberOfFrames,
    isFinished = isFinished
)

fun MatchSummary.toUi() = UiMatchSummary(
    match = match.toUi(),
    winner = winner.toUi(),
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak
)
