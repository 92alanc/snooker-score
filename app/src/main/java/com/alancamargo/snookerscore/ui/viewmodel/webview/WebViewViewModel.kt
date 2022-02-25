package com.alancamargo.snookerscore.ui.viewmodel.webview

import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel

class WebViewViewModel : ActionViewModel<WebViewUiAction>() {

    fun onStartLoading() {
        sendAction { WebViewUiAction.ShowLoading }
    }

    fun onFinishedLoading() {
        sendAction { WebViewUiAction.HideLoading }
    }

    fun onRefresh() {
        sendAction { WebViewUiAction.Refresh }
    }

}
