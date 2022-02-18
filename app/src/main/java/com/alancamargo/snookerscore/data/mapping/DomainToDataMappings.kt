package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.data.model.DbMatch
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.data.model.DbScore
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.model.Score

fun Player.toData() = DbPlayer(name)

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerName = player.name,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun Frame.toData() = DbFrame(id = id, matchDateTime = match.dateTime)

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Name = player1.name,
    player2Name = player2.name,
    numberOfFrames = numberOfFrames
)

fun Score.toData() = DbScore(
    id = id,
    frameId = frame.id,
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak
)