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

    return DbPlayer(id, name, genderId)
}

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerId = player.id,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)

fun Match.toData() = DbMatch(
    dateTime = dateTime,
    player1Id = player1.id,
    player2Id = player2.id,
    numberOfFrames = numberOfFrames
)

fun Frame.toData() = DbFrame(
    id = id,
    positionInMatch = positionInMatch,
    matchDateTime = match.dateTime,
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak,
    isFinished = isFinished
)