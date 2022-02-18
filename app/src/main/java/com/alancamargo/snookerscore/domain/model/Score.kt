package com.alancamargo.snookerscore.domain.model

data class Score(
    val id: String,
    val frame: Frame,
    val player1Score: Int,
    val player2Score: Int,
    val player1HighestBreak: Int,
    val player2HighestBreak: Int
)
