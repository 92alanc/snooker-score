package com.alancamargo.snookerscore.features.player.ui.viewmodel

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

sealed class PlayerListUiAction : UiAction {

    data class OpenPlayerStats(val player: UiPlayer) : PlayerListUiAction()

    object ShowNewPlayerDialogue : PlayerListUiAction()

    object ShowError : PlayerListUiAction()

    data class PickPlayer(val player: UiPlayer) : PlayerListUiAction()

    data class EditPlayer(val player: UiPlayer) : PlayerListUiAction()

    object ShowTip : PlayerListUiAction()

    object Finish : PlayerListUiAction()

}
