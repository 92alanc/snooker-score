package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Score(
    val id: String = UUID.randomUUID().toString(),
    val frame: Frame,
    var player1Score: Int = 0,
    var player2Score: Int = 0,
    var player1HighestBreak: Int = 0,
    var player2HighestBreak: Int = 0
)
