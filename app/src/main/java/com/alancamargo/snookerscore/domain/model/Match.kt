package com.alancamargo.snookerscore.domain.model

data class Match(
    val dateTime: Long,
    val player1: Player,
    val player2: Player,
    val numberOfFrames: Int
)
