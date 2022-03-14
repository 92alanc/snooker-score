package com.alancamargo.snookerscore.features.player.ui.mapping

import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.ui.model.UiGender
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

fun UiPlayer.toDomain() = Player(id = id, name = name, gender = gender.toDomain())

fun UiGender.toDomain() = when (this) {
    UiGender.MALE -> Gender.MALE
    UiGender.FEMALE -> Gender.FEMALE
}
