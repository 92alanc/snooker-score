package com.alancamargo.snookerscore.ui.viewmodel.matchlist

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiMatch

sealed class MatchListUiAction : UiAction {

    data class OpenMatchDetails(val match: UiMatch) : MatchListUiAction()

    object OpenNewMatch : MatchListUiAction()

    object ShowError : MatchListUiAction()

}
