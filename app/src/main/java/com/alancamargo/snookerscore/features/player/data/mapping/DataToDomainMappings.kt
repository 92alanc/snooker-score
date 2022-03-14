package com.alancamargo.snookerscore.features.player.data.mapping

import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player

const val GENDER_ID_MALE = 0
const val GENDER_ID_FEMALE = 1

fun DbPlayer.toDomain(): Player {
    val gender = when (genderId) {
        GENDER_ID_MALE -> Gender.MALE
        GENDER_ID_FEMALE -> Gender.FEMALE
        else -> throw IllegalArgumentException("Gender ID must be either 0 or 1")
    }

    return Player(id, name, gender)
}