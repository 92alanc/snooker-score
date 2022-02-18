package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MainViewModel() }
    viewModel { MatchListViewModel(getMatchesUseCase = get()) }
}