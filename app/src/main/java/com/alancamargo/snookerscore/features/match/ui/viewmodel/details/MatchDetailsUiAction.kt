package com.alancamargo.snookerscore.features.match.ui.viewmodel.details

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiFrame

sealed class MatchDetailsUiAction : UiAction {

    object ShowError : MatchDetailsUiAction()

    object Finish : MatchDetailsUiAction()

    object ShowDeleteMatchConfirmation : MatchDetailsUiAction()

    data class ViewSummary(val frames: List<UiFrame>) : MatchDetailsUiAction()

}
