package com.alancamargo.snookerscore.features.main.ui.viewmodel

import androidx.annotation.StringRes
import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class MainUiAction : UiAction {

    object OpenMatches : MainUiAction()

    object OpenPlayers : MainUiAction()

    data class OpenRules(@StringRes val screenTitleRes: Int, val url: String) : MainUiAction()

    object ShowAppInfo : MainUiAction()

    object Finish : MainUiAction()

}
