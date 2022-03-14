package com.alancamargo.snookerscore.features.player.data.mapping

import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player

fun Player.toData(): DbPlayer {
    val genderId = when (gender) {
        Gender.MALE -> GENDER_ID_MALE
        Gender.FEMALE -> GENDER_ID_FEMALE
    }

    return DbPlayer(id, name, genderId)
}
