package com.alancamargo.snookerscore.ui.viewmodel.matchsummary

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.ui.model.UiMatchSummary

data class MatchSummaryUiState(val matchSummary: UiMatchSummary? = null) : UiState {

    fun onMatchSummaryReceived(matchSummary: UiMatchSummary) = copy(matchSummary = matchSummary)

}
