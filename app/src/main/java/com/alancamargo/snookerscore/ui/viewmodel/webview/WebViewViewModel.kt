package com.alancamargo.snookerscore.ui.viewmodel.webview

import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel

class WebViewViewModel : ActionViewModel<WebViewUiAction>() {

    fun onStartLoading() {
        sendAction { WebViewUiAction.ShowLoading }
    }

    fun onFinishedLoading() {
        sendAction { WebViewUiAction.RenderPage }
    }

    fun onError() {
        sendAction { WebViewUiAction.ShowError }
    }

    fun onReloadClicked() {
        sendAction { WebViewUiAction.Reload }
    }

    fun onExitClicked() {
        sendAction { WebViewUiAction.Finish }
    }

}
