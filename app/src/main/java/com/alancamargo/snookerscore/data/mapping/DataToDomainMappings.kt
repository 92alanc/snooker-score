package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match

fun DbFrame.toDomain(match: Match) = Frame(
    id = id,
    positionInMatch = positionInMatch,
    match = match,
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak,
    isFinished = isFinished
)
