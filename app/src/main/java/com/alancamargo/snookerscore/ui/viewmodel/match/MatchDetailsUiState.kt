package com.alancamargo.snookerscore.ui.viewmodel.match

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiPlayer

data class MatchDetailsUiState(
    val winner: UiPlayer? = null,
    val frames: List<UiFrame>? = null,
    val isResumeMatchButtonEnabled: Boolean = false
) : UiState {

    fun onWinnerSet(winner: UiPlayer?) = copy(winner = winner)

    fun onFramesReceived(frames: List<UiFrame>) = copy(frames = frames)

    fun onEnableResumeMatchButton() = copy(isResumeMatchButtonEnabled = true)

}
