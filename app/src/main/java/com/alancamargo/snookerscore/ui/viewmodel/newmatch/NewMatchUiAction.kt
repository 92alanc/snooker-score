package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiMatch

sealed class NewMatchUiAction : UiAction {

    data class StartMatch(val match: UiMatch) : NewMatchUiAction()

    object ShowHelp : NewMatchUiAction()

    object ShowSamePlayersDialogue : NewMatchUiAction()

    object ShowLoading : NewMatchUiAction()

    object HideLoading : NewMatchUiAction()

    object ShowError : NewMatchUiAction()

    object ShowPlayers : NewMatchUiAction()

}
