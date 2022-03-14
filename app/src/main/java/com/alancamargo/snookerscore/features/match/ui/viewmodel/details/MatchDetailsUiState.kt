package com.alancamargo.snookerscore.features.match.ui.viewmodel.details

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.ui.model.UiFrame

data class MatchDetailsUiState(
    val winner: UiPlayer? = null,
    val frames: List<UiFrame>? = null
) : UiState {

    fun onWinnerSet(winner: UiPlayer?) = copy(winner = winner)

    fun onFramesReceived(frames: List<UiFrame>) = copy(frames = frames)

}
