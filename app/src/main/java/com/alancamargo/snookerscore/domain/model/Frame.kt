package com.alancamargo.snookerscore.domain.model

data class Frame(
    val id: String,
    val match: Match,
    val player1Score: Int,
    val player2Score: Int,
    val player1HighestBreak: Int,
    val player2HighestBreak: Int
)
