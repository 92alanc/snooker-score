package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.navigation.WebViewNavigation
import com.alancamargo.snookerscore.ui.navigation.WebViewNavigationImpl
import com.alancamargo.snookerscore.ui.viewmodel.webview.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory<WebViewNavigation> { WebViewNavigationImpl() }
    viewModel { WebViewViewModel() }
}