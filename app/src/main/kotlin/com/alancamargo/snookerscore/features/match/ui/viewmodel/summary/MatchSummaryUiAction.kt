package com.alancamargo.snookerscore.features.match.ui.viewmodel.summary

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class MatchSummaryUiAction : UiAction {

    object OpenMain : MatchSummaryUiAction()

    object NewMatch : MatchSummaryUiAction()

}
