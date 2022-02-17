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

fun Frame.toData() = DbFrame(
    id = id,
    matchDateTime = match.dateTime,
    player1ScoreId = player1Score.id,
    player2ScoreId = player2Score.id
)

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Id = player1.id,
    player1FinalScoreId = player1FinalScore.id,
    player2Id = player2.id,
    player2FinalScoreId = player2FinalScore.id,
    numberOfFrames = numberOfFrames
)