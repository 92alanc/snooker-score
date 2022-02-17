package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Frame(
    val id: String = UUID.randomUUID().toString(),
    val match: Match,
    val player1Score: Score = Score(),
    val player2Score: Score = Score()
)
