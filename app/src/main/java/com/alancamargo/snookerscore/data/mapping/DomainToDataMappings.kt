package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.domain.model.Player

fun Player.toData() = DbPlayer(id = id, name = name)