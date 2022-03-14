package com.alancamargo.snookerscore.features.match.ui.viewmodel.summary

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.features.match.ui.model.UiMatchSummary

data class MatchSummaryUiState(val matchSummary: UiMatchSummary? = null) : UiState {

    fun onMatchSummaryReceived(matchSummary: UiMatchSummary) = copy(matchSummary = matchSummary)

}
