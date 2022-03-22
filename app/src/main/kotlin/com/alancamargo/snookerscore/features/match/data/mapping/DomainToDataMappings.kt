package com.alancamargo.snookerscore.features.match.data.mapping

import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.data.model.DbMatch

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Id = player1.id,
    player2Id = player2.id,
    numberOfFrames = numberOfFrames,
    isFinished = isFinished
)
