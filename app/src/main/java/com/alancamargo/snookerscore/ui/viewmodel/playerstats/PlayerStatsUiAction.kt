package com.alancamargo.snookerscore.ui.viewmodel.playerstats

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class PlayerStatsUiAction : UiAction {

    object ShowError : PlayerStatsUiAction()

    object Finish : PlayerStatsUiAction()

    object ShowDeletePlayerConfirmation : PlayerStatsUiAction()

}
