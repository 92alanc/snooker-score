package com.alancamargo.snookerscore.features.playerstats.domain.model

import com.alancamargo.snookerscore.features.player.domain.model.Player

data class PlayerStats(
    val id: String,
    val player: Player,
    val matchesWon: Int,
    val highestScore: Int,
    val highestBreak: Int
)
