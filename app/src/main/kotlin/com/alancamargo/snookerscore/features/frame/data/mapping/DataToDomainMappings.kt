package com.alancamargo.snookerscore.features.frame.data.mapping

import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.frame.data.model.DbFrame
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
