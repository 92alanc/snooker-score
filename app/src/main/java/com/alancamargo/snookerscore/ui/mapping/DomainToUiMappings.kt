package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.features.match.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiFrame

fun Frame.toUi() = UiFrame(
    id = id,
    positionInMatch = positionInMatch,
    match = match.toUi(),
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak,
    isFinished = isFinished
)
