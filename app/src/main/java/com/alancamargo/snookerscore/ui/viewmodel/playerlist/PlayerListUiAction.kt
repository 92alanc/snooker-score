package com.alancamargo.snookerscore.ui.viewmodel.playerlist

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiPlayer

sealed class PlayerListUiAction : UiAction {

    data class SelectPlayer(val player: UiPlayer) : PlayerListUiAction()

    object ShowNewPlayerDialogue : PlayerListUiAction()

    object ShowLoading : PlayerListUiAction()

    object HideLoading : PlayerListUiAction()

    object ShowError : PlayerListUiAction()

}
