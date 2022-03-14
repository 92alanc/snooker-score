package com.alancamargo.snookerscore.features.webview.ui.viewmodel

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class WebViewUiAction : UiAction {

    object ShowLoading : WebViewUiAction()

    object HideLoading : WebViewUiAction()

    object Refresh : WebViewUiAction()

}
