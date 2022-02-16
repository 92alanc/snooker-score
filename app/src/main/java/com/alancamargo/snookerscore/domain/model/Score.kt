package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Score(
    val id: String = UUID.randomUUID().toString(),
    val player: Player,
    val score: Int,
    val highestBreak: Int
)
