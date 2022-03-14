package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.data.model.DbMatch
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Id = player1.id,
    player2Id = player2.id,
    numberOfFrames = numberOfFrames,
    isFinished = isFinished
)

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