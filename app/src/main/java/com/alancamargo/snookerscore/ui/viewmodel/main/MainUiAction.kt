package com.alancamargo.snookerscore.ui.viewmodel.main

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class MainUiAction : UiAction {

    object OpenMatches : MainUiAction()

    object OpenPlayers : MainUiAction()

    object OpenRules : MainUiAction()

    object ShowAppInfo : MainUiAction()

}
