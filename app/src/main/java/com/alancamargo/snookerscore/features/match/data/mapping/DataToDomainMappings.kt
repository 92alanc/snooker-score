package com.alancamargo.snookerscore.features.match.data.mapping

import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.data.model.DbMatch
import com.alancamargo.snookerscore.features.player.domain.model.Player

fun DbMatch.toDomain(player1: Player, player2: Player) = Match(
    dateTime = dateTime,
    player1 = player1,
    player2 = player2,
    numberOfFrames = numberOfFrames,
    isFinished = isFinished
)
