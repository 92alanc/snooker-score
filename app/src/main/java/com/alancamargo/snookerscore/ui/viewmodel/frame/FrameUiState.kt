package com.alancamargo.snookerscore.ui.viewmodel.frame

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiPlayer

data class FrameUiState(
    val currentFrame: UiFrame? = null,
    val currentPlayer: UiPlayer? = null
) : UiState {

    fun setCurrentFrame(currentFrame: UiFrame) = copy(currentFrame = currentFrame)

    fun setCurrentPlayer(currentPlayer: UiPlayer) = copy(currentPlayer = currentPlayer)

}
