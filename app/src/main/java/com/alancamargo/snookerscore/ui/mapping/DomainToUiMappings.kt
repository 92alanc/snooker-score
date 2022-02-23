package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Gender
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.ui.model.UiGender
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.model.UiPlayer
import com.alancamargo.snookerscore.ui.model.UiPlayerStats

fun Player.toUi() = UiPlayer(name = name, gender = gender.toUi())

fun Gender.toUi() = when (this) {
    Gender.MALE -> UiGender.MALE
    Gender.FEMALE -> UiGender.FEMALE
}

fun Match.toUi() = UiMatch(
    dateTime = dateTime,
    player1 = player1.toUi(),
    player2 = player2.toUi(),
    numberOfFrames = numberOfFrames
)

fun PlayerStats.toUi() = UiPlayerStats(
    id = id,
    player = player.toUi(),
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)