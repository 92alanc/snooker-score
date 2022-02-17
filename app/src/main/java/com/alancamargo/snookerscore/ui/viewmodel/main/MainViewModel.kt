package com.alancamargo.snookerscore.ui.viewmodel.main

import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel

class MainViewModel : ActionViewModel<MainUiAction>() {

    fun onClickMatches() {
        sendAction { MainUiAction.OpenMatches }
    }

    fun onClickPlayers() {
        sendAction { MainUiAction.OpenPlayers }
    }

    fun onClickRules() {
        sendAction { MainUiAction.OpenRules }
    }

    fun onClickAbout() {
        sendAction { MainUiAction.ShowAppInfo }
    }

}
