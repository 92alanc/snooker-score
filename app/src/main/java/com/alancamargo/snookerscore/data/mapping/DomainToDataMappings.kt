package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.data.model.DbMatch
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats

fun Player.toData() = DbPlayer(id = id, name = name)

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerId = player.id,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun Frame.toData() = DbFrame(id = id, matchDateTime = match.dateTime)

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Id = player1.id,
    player2Id = player2.id,
    numberOfFrames = numberOfFrames
)