package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.model.UiFrame

fun UiFrame.toDomain() = Frame(
    id = id,
    positionInMatch = positionInMatch,
    match = match.toDomain(),
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak,
    isFinished = isFinished
)