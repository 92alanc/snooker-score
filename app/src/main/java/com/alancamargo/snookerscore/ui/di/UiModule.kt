package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.alancamargo.snookerscore.navigation.PlayerStatsNavigation
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import com.alancamargo.snookerscore.ui.navigation.PlayerListNavigationImpl
import com.alancamargo.snookerscore.ui.navigation.PlayerStatsNavigationImpl
import com.alancamargo.snookerscore.ui.navigation.WebViewNavigationImpl
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameViewModel
import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsViewModel
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListViewModel
import com.alancamargo.snookerscore.ui.viewmodel.newmatch.NewMatchViewModel
import com.alancamargo.snookerscore.ui.viewmodel.playerlist.PlayerListViewModel
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsViewModel
import com.alancamargo.snookerscore.ui.viewmodel.webview.WebViewViewModel
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
                playerUseCases = FrameViewModel.PlayerUseCases(
                    drawPlayerUseCase = get(),
                    getWinningPlayerUseCase = get()
                ),
                playerStatsUseCases = FrameViewModel.PlayerStatsUseCases(
                    getPlayerStatsUseCase = get(),
                    addOrUpdatePlayerStatsUseCase = get(),
                    updatePlayerStatsWithMatchResultUseCase = get()
                ),
                addOrUpdateFrameUseCase = get(),
                getPenaltyValueUseCase = get(),
                deleteMatchUseCase = get()
            ),
            breakCalculator = get(),
        )
    }
    viewModel { MatchDetailsViewModel(deleteMatchUseCase = get()) }
    viewModel { params ->
        PlayerListViewModel(
            isPickingPlayer = params.get(),
            addOrUpdatePlayerUseCase = get(),
            addOrUpdatePlayerStatsUseCase = get(),
            getPlayersUseCase = get()
        )
    }
    viewModel {
        PlayerStatsViewModel(
            getPlayerStatsUseCase = get(),
            deletePlayerUseCase = get()
        )
    }
    factory<WebViewNavigation> { WebViewNavigationImpl() }
    viewModel { WebViewViewModel() }
    factory<PlayerListNavigation> { PlayerListNavigationImpl() }
    factory<PlayerStatsNavigation> { PlayerStatsNavigationImpl() }
}