package com.alancamargo.snookerscore.features.main.ui.viewmodel

import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel
import com.alancamargo.snookerscore.features.main.data.analytics.MainAnalytics
import com.alancamargo.snookerscore.features.main.domain.usecase.GetRulesUrlUseCase

class MainViewModel(
    private val getRulesUrlUseCase: GetRulesUrlUseCase,
    private val analytics: MainAnalytics
) : ActionViewModel<MainUiAction>() {

    init {
        analytics.trackScreenViewed()
    }

    fun onClickMatches() {
        analytics.trackMatchesClicked()
        sendAction { MainUiAction.OpenMatches }
    }

    fun onClickPlayers() {
        analytics.trackPlayersClicked()
        sendAction { MainUiAction.OpenPlayers }
    }

    fun onClickRules() {
        analytics.trackRulesClicked()

        val url = getRulesUrlUseCase()
        sendAction { MainUiAction.OpenRules(R.string.rules_web_view_title, url) }
    }

    fun onClickAbout() {
        analytics.trackAboutClicked()
        sendAction { MainUiAction.ShowAppInfo }
    }

    fun onClickBack() {
        analytics.trackNativeBackButtonClicked()
        sendAction { MainUiAction.Finish }
    }

}
