package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListViewModel
import com.alancamargo.snookerscore.ui.viewmodel.newmatch.NewMatchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MainViewModel() }
    viewModel { MatchListViewModel(getMatchesUseCase = get()) }
    viewModel {
        NewMatchViewModel(
            getPlayersUseCase = get(),
            arePlayersTheSameUseCase = get(),
            addMatchUseCase = get()
        )
    }
}