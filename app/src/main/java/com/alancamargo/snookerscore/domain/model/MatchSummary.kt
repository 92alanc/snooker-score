package com.alancamargo.snookerscore.domain.model

data class MatchSummary(
    val match: Match,
    val winner: Player,
    val player1Score: Int,
    val player2Score: Int,
    val player1HighestBreak: Int,
    val player2HighestBreak: Int
)
