package com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

data class NewMatchUiState(
    val player1: UiPlayer? = null,
    val player2: UiPlayer? = null,
    val numberOfFrames: Int,
    val isStartMatchButtonEnabled: Boolean = false
) : UiState {

    fun onPlayer1Selected(player1: UiPlayer) = copy(player1 = player1)

    fun onPlayer2Selected(player2: UiPlayer) = copy(player2 = player2)

    fun onNumberOfFramesChanged(numberOfFrames: Int) = copy(numberOfFrames = numberOfFrames)

    fun onEnableStartMatchButton() = copy(isStartMatchButtonEnabled = true)

    fun onDisableStartMatchButton() = copy(isStartMatchButtonEnabled = false)

}
