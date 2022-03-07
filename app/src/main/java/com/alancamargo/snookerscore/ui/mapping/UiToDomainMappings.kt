package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Gender
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiGender
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.model.UiPlayer

fun UiPlayer.toDomain() = Player(id = id, name = name, gender = gender.toDomain())

fun UiGender.toDomain() = when (this) {
    UiGender.MALE -> Gender.MALE
    UiGender.FEMALE -> Gender.FEMALE
}

fun UiMatch.toDomain() = Match(
    dateTime = dateTime,
    player1 = player1.toDomain(),
    player2 = player2.toDomain(),
    numberOfFrames = numberOfFrames
)

fun UiFrame.toDomain() = Frame(
    id = id,
    positionInMatch = positionInMatch,
    match = match.toDomain(),
    player1Score = player1Score,
    player2Score = player2Score,
    player1HighestBreak = player1HighestBreak,
    player2HighestBreak = player2HighestBreak,
    isFinished = isFinished
)