package com.alancamargo.snookerscore.ui.viewmodel.webview

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class WebViewUiAction : UiAction {

    object ShowLoading : WebViewUiAction()

    object HideLoading : WebViewUiAction()

    object Refresh : WebViewUiAction()

}
