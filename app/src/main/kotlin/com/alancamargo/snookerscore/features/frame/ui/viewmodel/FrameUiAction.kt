package com.alancamargo.snookerscore.features.frame.ui.viewmodel

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

sealed class FrameUiAction : UiAction {

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

    object ShowFullScreenAds : FrameUiAction()

}
