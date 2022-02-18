package com.alancamargo.snookerscore.ui.mapping

import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.ui.model.UiPlayer

fun UiPlayer.toDomain() = Player(name = name)