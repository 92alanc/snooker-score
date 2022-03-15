package com.alancamargo.snookerscore.features.webview.ui.viewmodel

import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel
import com.alancamargo.snookerscore.features.webview.data.analytics.WebViewAnalytics

class WebViewViewModel(
    private val analytics: WebViewAnalytics
) : ActionViewModel<WebViewUiAction>() {

    init {
        analytics.trackScreenViewed()
    }

    fun onStartLoading() {
        sendAction { WebViewUiAction.ShowLoading }
    }

    fun onFinishedLoading() {
        sendAction { WebViewUiAction.HideLoading }
    }

    fun onRefresh() {
        sendAction { WebViewUiAction.Refresh }
    }

    fun onBackClicked() {
        analytics.trackBackClicked()
        sendAction { WebViewUiAction.Finish }
    }

    fun onNativeBackClicked() {
        analytics.trackNativeBackClicked()
        sendAction { WebViewUiAction.Finish }
    }

}
