package com.alancamargo.snookerscore.ui.viewmodel.matchlist

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiMatch

data class MatchListUiState(val matches: List<UiMatch> = emptyList()) : UiState {

    fun onMatchesReceived(matches: List<UiMatch>) = copy(matches = matches)

}
