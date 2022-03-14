package com.alancamargo.snookerscore.domain.model

import com.alancamargo.snookerscore.features.player.domain.model.Player

data class Match(
    val dateTime: Long = System.currentTimeMillis(),
    val player1: Player,
    val player2: Player,
    val numberOfFrames: Int,
    val isFinished: Boolean = false
)
