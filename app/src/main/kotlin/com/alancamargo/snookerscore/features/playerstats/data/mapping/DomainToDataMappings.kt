package com.alancamargo.snookerscore.features.playerstats.data.mapping

import com.alancamargo.snookerscore.features.playerstats.data.model.DbPlayerStats
import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats

fun PlayerStats.toData() = DbPlayerStats(
    id = id,
    playerId = player.id,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)
