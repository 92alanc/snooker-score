package com.alancamargo.snookerscore.features.player.ui.viewmodel

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

data class PlayerListUiState(val players: List<UiPlayer>? = null) : UiState {

    fun onPlayersReceived(players: List<UiPlayer>) = copy(players = players)

}
