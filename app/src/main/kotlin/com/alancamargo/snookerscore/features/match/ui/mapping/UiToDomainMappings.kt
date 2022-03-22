package com.alancamargo.snookerscore.features.match.ui.mapping

import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.player.ui.mapping.toDomain
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch

fun UiMatch.toDomain() = Match(
    dateTime = dateTime,
    player1 = player1.toDomain(),
    player2 = player2.toDomain(),
    numberOfFrames = numberOfFrames,
    isFinished = isFinished
)
