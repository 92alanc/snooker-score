package com.alancamargo.snookerscore.data.mapping

import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats

fun Player.toData() = DbPlayer(id = id, name = name)

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerId = player.id,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)