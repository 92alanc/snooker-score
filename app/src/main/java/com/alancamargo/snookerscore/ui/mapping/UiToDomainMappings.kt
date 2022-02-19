package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.ui.model.UiBall
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.model.UiPlayer

fun UiPlayer.toDomain() = Player(name = name)

fun UiBall.toDomain() = when (this) {
    UiBall.CUE_BALL -> Ball.CUE_BALL
    UiBall.RED -> Ball.RED
    UiBall.YELLOW -> Ball.YELLOW
    UiBall.GREEN -> Ball.GREEN
    UiBall.BROWN -> Ball.BROWN
    UiBall.BLUE -> Ball.BLUE
    UiBall.PINK -> Ball.PINK
    UiBall.BLACK -> Ball.BLACK
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
    player2HighestBreak = player2HighestBreak
)