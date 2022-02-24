package com.alancamargo.snookerscore.ui.viewmodel.main

import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel
import com.alancamargo.snookerscore.domain.usecase.rules.GetRulesUrlUseCase

class MainViewModel(
    private val getRulesUrlUseCase: GetRulesUrlUseCase
) : ActionViewModel<MainUiAction>() {

    fun onClickMatches() {
        sendAction { MainUiAction.OpenMatches }
    }

    fun onClickPlayers() {
        sendAction { MainUiAction.OpenPlayers }
    }

    fun onClickRules() {
        val url = getRulesUrlUseCase()
        sendAction { MainUiAction.OpenRules(R.string.rules_web_view_title, url) }
    }

    fun onClickAbout() {
        sendAction { MainUiAction.ShowAppInfo }
    }

}
