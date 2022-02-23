package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.data.model.DbMatch
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Gender
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats

const val GENDER_ID_MALE = 0
const val GENDER_ID_FEMALE = 1

fun DbFrame.toDomain(match: Match) = Frame(
    id = id,
    positionInMatch = positionInMatch,
    match = match,
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak
)

fun DbPlayer.toDomain(): Player {
    val gender = when (genderId) {
        GENDER_ID_MALE -> Gender.MALE
        GENDER_ID_FEMALE -> Gender.FEMALE
        else -> throw IllegalArgumentException("Gender ID must be either 0 or 1")
    }

    return Player(name, gender)
}

fun DbPlayerStats.toDomain(player: Player) = PlayerStats(
    id = id,
    player = player,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun DbMatch.toDomain(player1: Player, player2: Player) = Match(
    dateTime = dateTime,
    player1 = player1,
    player2 = player2,
    numberOfFrames = numberOfFrames
)