package com.alancamargo.snookerscore.ui.viewmodel.match

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class MatchDetailsUiAction : UiAction {

    object ShowLoading : MatchDetailsUiAction()

    object HideLoading : MatchDetailsUiAction()

    object ShowError : MatchDetailsUiAction()

    object Finish : MatchDetailsUiAction()

}
