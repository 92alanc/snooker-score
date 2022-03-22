package com.alancamargo.snookerscore.features.webview.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.features.webview.data.analytics.WebViewAnalytics
import com.alancamargo.snookerscore.features.webview.data.analytics.WebViewAnalyticsImpl
import com.alancamargo.snookerscore.features.webview.ui.navigation.WebViewNavigationImpl
import com.alancamargo.snookerscore.features.webview.ui.viewmodel.WebViewViewModel
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class WebViewModule : FeatureModule() {

    override val data = module {
        factory<WebViewAnalytics> { WebViewAnalyticsImpl(analytics = get()) }
    }

    override val ui = module {
        viewModel { WebViewViewModel(analytics = get()) }
        factory<WebViewNavigation> { WebViewNavigationImpl() }
    }

}
