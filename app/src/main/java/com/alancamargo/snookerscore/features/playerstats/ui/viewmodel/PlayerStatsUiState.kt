package com.alancamargo.snookerscore.features.playerstats.ui.viewmodel

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiPlayerStats

data class PlayerStatsUiState(val playerStats: UiPlayerStats? = null) : UiState {

    fun onPlayerStatsReceived(playerStats: UiPlayerStats) = copy(playerStats = playerStats)

}
