package com.alancamargo.snookerscore.ui.viewmodel.frame

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiPlayer

sealed class FrameUiAction : UiAction {

    object ShowLoading : FrameUiAction()

    object HideLoading : FrameUiAction()

    object ShowError : FrameUiAction()

    data class OpenMatchSummary(val frames: List<UiFrame>) : FrameUiAction()

    object OpenMain : FrameUiAction()

    object ShowEndFrameConfirmation : FrameUiAction()

    object ShowForfeitMatchConfirmation : FrameUiAction()

    object ShowObjectBalls : FrameUiAction()

    data class ShowStartingPlayerPrompt(
        val player1: UiPlayer,
        val player2: UiPlayer
    ) : FrameUiAction()

}
