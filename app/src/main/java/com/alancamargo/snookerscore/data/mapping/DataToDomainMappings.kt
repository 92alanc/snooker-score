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
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun DbFrame.toDomain() = Frame(
    id = id,
    player1Score = Json.decodeFromString(player1ScoreJson),
    player2Score = Json.decodeFromString(player2ScoreJson)
)

fun DbScore.toDomain() = Score(
    id = id,
    player = Json.decodeFromString(playerJson),
    score = score,
    highestBreak = highestBreak
)

fun DbPlayer.toDomain() = Player(id = id, name = name)

fun DbPlayerStats.toDomain() = PlayerStats(
    id = id,
    player = Json.decodeFromString(playerJson),
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun DbMatch.toDomain() = Match(
    id = id,
    frames = Json.decodeFromString(framesJson)
)