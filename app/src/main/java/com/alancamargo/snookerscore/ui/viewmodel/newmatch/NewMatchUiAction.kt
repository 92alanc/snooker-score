package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiFrame

sealed class NewMatchUiAction : UiAction {

    data class StartMatch(val frames: List<UiFrame>) : NewMatchUiAction()

    object ShowSamePlayersDialogue : NewMatchUiAction()

    object ShowLoading : NewMatchUiAction()

    object HideLoading : NewMatchUiAction()

    object ShowError : NewMatchUiAction()

    object PickPlayer : NewMatchUiAction()

}
