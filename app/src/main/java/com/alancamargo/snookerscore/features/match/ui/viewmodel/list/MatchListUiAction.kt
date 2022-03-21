package com.alancamargo.snookerscore.features.match.ui.viewmodel.list

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch

sealed class MatchListUiAction : UiAction {

    data class OpenMatchDetails(val match: UiMatch) : MatchListUiAction()

    object OpenNewMatch : MatchListUiAction()

    object ShowError : MatchListUiAction()

    object Finish : MatchListUiAction()

}
