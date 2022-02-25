package com.alancamargo.snookerscore.ui.viewmodel.webview

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction

sealed class WebViewUiAction : UiAction {

    object ShowLoading : WebViewUiAction()

    object RenderPage : WebViewUiAction()

    object ShowError : WebViewUiAction()

    object Reload : WebViewUiAction()

    object Finish : WebViewUiAction()

}
