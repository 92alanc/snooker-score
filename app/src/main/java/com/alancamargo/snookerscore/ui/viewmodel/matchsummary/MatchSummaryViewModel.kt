package com.alancamargo.snookerscore.ui.viewmodel.matchsummary

import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel

class MatchSummaryViewModel : ActionViewModel<MatchSummaryUiAction>() {

    fun onCloseButtonClicked() {
        sendAction { MatchSummaryUiAction.OpenMain }
    }

    fun onNewMatchButtonClicked() {
        sendAction { MatchSummaryUiAction.NewMatch }
    }

}
