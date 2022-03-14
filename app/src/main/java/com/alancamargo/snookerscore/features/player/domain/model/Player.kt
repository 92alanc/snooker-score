package com.alancamargo.snookerscore.features.player.domain.model

import java.util.UUID

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val gender: Gender
)
