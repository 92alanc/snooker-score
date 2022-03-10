package com.alancamargo.snookerscore.ui.viewmodel.matchsummary

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class MatchSummaryUiAction : UiAction {

    object OpenMain : MatchSummaryUiAction()

    object NewMatch : MatchSummaryUiAction()

}
