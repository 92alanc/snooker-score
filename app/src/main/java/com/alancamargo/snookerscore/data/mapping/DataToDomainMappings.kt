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

fun DbFrame.toDomain() = Frame(
    id = id,
    player1Score = player1Score.toDomain(),
    player2Score = player2Score.toDomain()
)

fun DbScore.toDomain() = Score(
    id = id,
    player = player.toDomain(),
    score = score,
    highestBreak = highestBreak
)

fun DbPlayer.toDomain() = Player(id = id, name = name)

fun DbPlayerStats.toDomain() = PlayerStats(
    id = id,
    player = player.toDomain(),
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun DbMatch.toDomain() = Match(
    id = id,
    frames = emptyList() // TODO: convert from JSON to Frame
)