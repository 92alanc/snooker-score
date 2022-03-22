package com.alancamargo.snookerscore.features.frame.data.mapping

import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.frame.data.model.DbFrame

fun Frame.toData() = DbFrame(
    id = id,
    positionInMatch = positionInMatch,
    matchDateTime = match.dateTime,
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak,
    isFinished = isFinished
)