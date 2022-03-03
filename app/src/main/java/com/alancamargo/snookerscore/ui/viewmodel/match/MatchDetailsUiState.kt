package com.alancamargo.snookerscore.ui.viewmodel.match

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiFrame

data class MatchDetailsUiState(val frames: List<UiFrame>? = null) : UiState {

    fun onFramesReceived(frames: List<UiFrame>) = copy(frames = frames)

}
