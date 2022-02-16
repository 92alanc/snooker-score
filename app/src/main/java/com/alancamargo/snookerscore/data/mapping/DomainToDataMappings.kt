package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.mapping.json.PlayerSerialiser
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import kotlinx.serialization.json.Json

fun Player.toData() = DbPlayer(id = id, name = name)

fun Player.toJson() = Json.encodeToString(PlayerSerialiser, value = this)

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerJson = player.toJson(),
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)