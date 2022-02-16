package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Frame(
    val id: String = UUID.randomUUID().toString(),
    val player1Score: Score,
    val player2Score: Score
)
