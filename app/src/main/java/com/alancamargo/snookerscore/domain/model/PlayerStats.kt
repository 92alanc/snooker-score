package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class PlayerStats(
    val id: String = UUID.randomUUID().toString(),
    val player: Player,
    val matchesWon: Int = 0,
    val highestScore: Int = 0,
    val highestBreak: Int = 0
)
