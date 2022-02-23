package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.navigation.WebsiteNavigation
import com.alancamargo.snookerscore.ui.navigation.WebsiteNavigationImpl
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameViewModel
import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsViewModel
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListViewModel
import com.alancamargo.snookerscore.ui.viewmodel.newmatch.NewMatchViewModel
import com.alancamargo.snookerscore.ui.viewmodel.playerlist.PlayerListViewModel
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MainViewModel(getRulesUrlUseCase = get()) }
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
                deleteMatchUseCase = get(),
                getPlayerStatsUseCase = get(),
                addOrUpdatePlayerStatsUseCase = get(),
                getWinningPlayerUseCase = get(),
                updatePlayerStatsWithMatchResultUseCase = get()
            ),
            breakCalculator = get(),
        )
    }
    viewModel { MatchDetailsViewModel(deleteMatchUseCase = get()) }
    viewModel {
        PlayerListViewModel(
            addOrUpdatePlayerUseCase = get(),
            deletePlayerUseCase = get(),
            getPlayersUseCase = get()
        )
    }
    viewModel { PlayerStatsViewModel(getPlayerStatsUseCase = get()) }
    factory<WebsiteNavigation> { WebsiteNavigationImpl() }
}