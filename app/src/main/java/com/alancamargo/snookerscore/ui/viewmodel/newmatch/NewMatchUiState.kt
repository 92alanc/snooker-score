package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiPlayer

data class NewMatchUiState(
    val players: List<UiPlayer> = emptyList(),
    val isStartMatchButtonEnabled: Boolean = false
) : UiState {

    fun onPlayersReceived(players: List<UiPlayer>) = copy(players = players)

    fun onEnableStartMatchButton() = copy(isStartMatchButtonEnabled = true)

    fun onDisableStartMatchButton() = copy(isStartMatchButtonEnabled = false)

}
