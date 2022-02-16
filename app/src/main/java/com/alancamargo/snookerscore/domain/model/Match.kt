package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Match(
    val id: String = UUID.randomUUID().toString(),
    val player1: Player,
    val player2: Player,
    val frames: List<Frame>
)
