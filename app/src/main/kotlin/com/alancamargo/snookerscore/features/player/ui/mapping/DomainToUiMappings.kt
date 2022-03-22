package com.alancamargo.snookerscore.features.player.ui.mapping

import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.ui.model.UiGender
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

fun Player.toUi() = UiPlayer(id = id, name = name, gender = gender.toUi())

fun Gender.toUi() = when (this) {
    Gender.MALE -> UiGender.MALE
    Gender.FEMALE -> UiGender.FEMALE
}
