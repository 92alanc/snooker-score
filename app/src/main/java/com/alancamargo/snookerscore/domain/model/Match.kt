package com.alancamargo.snookerscore.domain.model

data class Match(
    val dateTime: Long = System.currentTimeMillis(),
    val player1: Player,
    val player1FinalScore: Score = Score(),
    val player2: Player,
    val player2FinalScore: Score = Score(),
    val numberOfFrames: Int
)
