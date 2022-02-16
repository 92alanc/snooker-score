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

fun DbFrame.toDomain(player1Score: Score, player2Score: Score) = Frame(
    id = id,
    player1Score = player1Score,
    player2Score = player2Score
)

fun DbScore.toDomain() = Score(
    id = id,
    score = score,
    highestBreak = highestBreak
)

fun DbPlayer.toDomain() = Player(id = id, name = name)

fun DbPlayerStats.toDomain(player: Player) = PlayerStats(
    id = id,
    player = player,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun DbMatch.toDomain(player1: Player, player2: Player, frames: List<Frame>) = Match(
    id = id,
    player1 = player1,
    player2 = player2,
    frames = frames
)