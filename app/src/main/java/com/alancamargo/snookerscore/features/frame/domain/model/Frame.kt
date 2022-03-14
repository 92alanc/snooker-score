package com.alancamargo.snookerscore.features.frame.domain.model

import com.alancamargo.snookerscore.features.match.domain.model.Match
import java.util.UUID

data class Frame(
    val id: String = UUID.randomUUID().toString(),
    val positionInMatch: Int,
    val match: Match,
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val player1HighestBreak: Int = 0,
    val player2HighestBreak: Int = 0,
    val isFinished: Boolean = false
)
