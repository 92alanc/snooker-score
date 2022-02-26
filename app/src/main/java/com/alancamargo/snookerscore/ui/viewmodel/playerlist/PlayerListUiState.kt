package com.alancamargo.snookerscore.ui.viewmodel.playerlist

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiPlayer

data class PlayerListUiState(val players: List<UiPlayer>? = null) : UiState {

    fun onPlayersReceived(players: List<UiPlayer>) = copy(players = players)

}
