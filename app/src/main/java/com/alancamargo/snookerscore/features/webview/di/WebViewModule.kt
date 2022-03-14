package com.alancamargo.snookerscore.features.webview.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.features.webview.ui.navigation.WebViewNavigationImpl
import com.alancamargo.snookerscore.features.webview.ui.viewmodel.WebViewViewModel
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class WebViewModule : FeatureModule() {

    override val ui = module {
        viewModel { WebViewViewModel() }
        factory<WebViewNavigation> { WebViewNavigationImpl() }
    }

}
