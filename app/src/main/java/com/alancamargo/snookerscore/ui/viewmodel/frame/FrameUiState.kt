package com.alancamargo.snookerscore.ui.viewmodel.frame

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

data class FrameUiState(
    val currentFrame: UiFrame? = null,
    val currentPlayer: UiPlayer? = null,
    val isUndoLastPottedBallButtonEnabled: Boolean = false,
    val isUndoLastFoulButtonEnabled: Boolean = false,
    val breakValue: Int = 0,
    val player1Score: Int = 0,
    val player2Score: Int = 0
) : UiState {

    fun setCurrentFrame(currentFrame: UiFrame) = copy(
        currentFrame = currentFrame,
        player1Score = currentFrame.player1Score,
        player2Score = currentFrame.player2Score
    )

    fun setCurrentPlayer(currentPlayer: UiPlayer) = copy(currentPlayer = currentPlayer)

    fun onEnableUndoLastPottedBallButton() = copy(isUndoLastPottedBallButtonEnabled = true)

    fun onDisableUndoLastPottedBallButton() = copy(isUndoLastPottedBallButtonEnabled = false)

    fun onEnableUndoLastFoulButton() = copy(isUndoLastFoulButtonEnabled = true)

    fun onDisableUndoLastFoulButton() = copy(isUndoLastFoulButtonEnabled = false)

    fun onBreakUpdated(breakValue: Int) = copy(breakValue = breakValue)

    fun onPlayer1ScoreUpdated(player1Score: Int) = copy(player1Score = player1Score)

    fun onPlayer2ScoreUpdated(player2Score: Int) = copy(player2Score = player2Score)

}
