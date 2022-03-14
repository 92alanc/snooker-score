package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.data.model.DbMatch
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.features.player.domain.model.Player

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

fun DbMatch.toDomain(player1: Player, player2: Player) = Match(
    dateTime = dateTime,
    player1 = player1,
    player2 = player2,
    numberOfFrames = numberOfFrames,
    isFinished = isFinished
)