package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.model.UiPlayer

fun Player.toUi() = UiPlayer(name = name)

fun Match.toUi() = UiMatch(
    dateTime = dateTime,
    player1 = player1.toUi(),
    player2 = player2.toUi(),
    numberOfFrames = numberOfFrames
)