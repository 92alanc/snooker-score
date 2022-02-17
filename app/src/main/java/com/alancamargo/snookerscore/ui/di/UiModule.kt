package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MainViewModel() }
}