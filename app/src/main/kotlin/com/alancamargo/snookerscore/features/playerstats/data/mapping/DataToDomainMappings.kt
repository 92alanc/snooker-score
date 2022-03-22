package com.alancamargo.snookerscore.features.playerstats.data.mapping

import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.playerstats.data.model.DbPlayerStats
import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats

fun DbPlayerStats.toDomain(player: Player) = PlayerStats(
    id = id,
    player = player,
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)
