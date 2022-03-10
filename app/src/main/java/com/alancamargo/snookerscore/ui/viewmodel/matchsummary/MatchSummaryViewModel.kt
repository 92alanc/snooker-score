package com.alancamargo.snookerscore.ui.viewmodel.matchsummary

import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.usecase.match.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiFrame

class MatchSummaryViewModel(
    frames: List<UiFrame>,
    getMatchSummaryUseCase: GetMatchSummaryUseCase
) : ViewModel<MatchSummaryUiState, MatchSummaryUiAction>(initialState = MatchSummaryUiState()) {

    init {
        val domainFrames = frames.map { it.toDomain() }
        val matchSummary = getMatchSummaryUseCase(domainFrames).toUi()
        setState { state -> state.onMatchSummaryReceived(matchSummary) }
    }

    fun onCloseButtonClicked() {
        sendAction { MatchSummaryUiAction.OpenMain }
    }

    fun onNewMatchButtonClicked() {
        sendAction { MatchSummaryUiAction.NewMatch }
    }

}
