package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameViewModel
import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsViewModel
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
    viewModel { parameters ->
        FrameViewModel(
            frames = parameters.get(),
            useCases = FrameViewModel.UseCases(
                drawPlayerUseCase = get(),
                addOrUpdateFrameUseCase = get(),
                getPenaltyValueUseCase = get(),
                deleteMatchUseCase = get()
            ),
            breakCalculator = get(),
        )
    }
    viewModel { MatchDetailsViewModel(deleteMatchUseCase = get()) }
}