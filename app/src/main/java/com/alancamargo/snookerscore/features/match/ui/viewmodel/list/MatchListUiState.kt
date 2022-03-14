package com.alancamargo.snookerscore.features.match.ui.viewmodel.list

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch

data class MatchListUiState(val matches: List<UiMatch>? = null) : UiState {

    fun onMatchesReceived(matches: List<UiMatch>) = copy(matches = matches)

}
