package com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame

sealed class NewMatchUiAction : UiAction {

    data class StartMatch(val frames: List<UiFrame>) : NewMatchUiAction()

    object ShowSamePlayersDialogue : NewMatchUiAction()

    object ShowError : NewMatchUiAction()

    object PickPlayer : NewMatchUiAction()

    object Finish : NewMatchUiAction()

}
