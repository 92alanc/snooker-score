package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Score(
    val id: String = UUID.randomUUID().toString(),
    var score: Int = 0,
    var highestBreak: Int = 0
)
