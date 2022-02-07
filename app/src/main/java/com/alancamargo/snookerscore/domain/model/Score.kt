package com.alancamargo.snookerscore.domain.model

data class Score(val id: String, val player: Player, val score: Int, val highestBreak: Int)
