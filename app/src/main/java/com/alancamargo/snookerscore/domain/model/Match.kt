package com.alancamargo.snookerscore.domain.model

import java.util.UUID

data class Match(
    val id: String = UUID.randomUUID().toString(),
    val frames: List<Frame>
)
