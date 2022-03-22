package com.alancamargo.snookerscore.features.match.ui.viewmodel.summary

import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.features.frame.ui.mapping.toDomain
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.match.data.analytics.summary.MatchSummaryAnalytics
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.features.match.ui.mapping.toUi

class MatchSummaryViewModel(
    frames: List<UiFrame>,
    getMatchSummaryUseCase: GetMatchSummaryUseCase,
    private val analytics: MatchSummaryAnalytics
) : ViewModel<MatchSummaryUiState, MatchSummaryUiAction>(initialState = MatchSummaryUiState()) {

    init {
        analytics.trackScreenViewed()

        val domainFrames = frames.map { it.toDomain() }
        val matchSummary = getMatchSummaryUseCase(domainFrames).toUi()
        setState { state -> state.onMatchSummaryReceived(matchSummary) }
    }

    fun onBackClicked() {
        analytics.trackBackClicked()
        sendAction { MatchSummaryUiAction.OpenMain }
    }

    fun onNativeBackClicked() {
        analytics.trackNativeBackClicked()
        sendAction { MatchSummaryUiAction.OpenMain }
    }

    fun onNewMatchButtonClicked() {
        analytics.trackNewMatchClicked()
        sendAction { MatchSummaryUiAction.NewMatch }
    }

}
