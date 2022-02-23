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

fun Player.toData(): DbPlayer {
    val genderId = when (gender) {
        Gender.MALE -> GENDER_ID_MALE
        Gender.FEMALE -> GENDER_ID_FEMALE
    }

    return DbPlayer(name, genderId)
}

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerName = player.name,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Name = player1.name,
    player2Name = player2.name,
    numberOfFrames = numberOfFrames
)

fun Frame.toData() = DbFrame(
    id = id,
    positionInMatch = positionInMatch,
    matchDateTime = match.dateTime,
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak
)