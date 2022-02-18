package com.alancamargo.snookerscore.domain.model

data class PlayerStats(
    val id: String,
    val player: Player,
    val matchesWon: Int,
    val highestScore: Int,
    val highestBreak: Int
)
