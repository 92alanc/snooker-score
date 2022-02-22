package com.alancamargo.snookerscore.ui.viewmodel.main

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class MainUiAction : UiAction {

    object OpenMatches : MainUiAction()

    object OpenPlayers : MainUiAction()

    data class OpenRules(val url: String) : MainUiAction()

    object ShowAppInfo : MainUiAction()

}
