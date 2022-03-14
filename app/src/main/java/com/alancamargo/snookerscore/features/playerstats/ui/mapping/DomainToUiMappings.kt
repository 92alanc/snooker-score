package com.alancamargo.snookerscore.features.playerstats.ui.mapping

import com.alancamargo.snookerscore.features.playerstats.domain.model.PlayerStats
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.features.playerstats.ui.model.UiPlayerStats

fun PlayerStats.toUi() = UiPlayerStats(
    id = id,
    player = player.toUi(),
    matchesWon = matchesWon,
    highestScore = highestScore,
    highestBreak = highestBreak
)
