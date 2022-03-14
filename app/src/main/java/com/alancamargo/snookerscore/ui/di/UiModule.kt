package com.alancamargo.snookerscore.ui.di

import com.alancamargo.snookerscore.navigation.FrameNavigation
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.navigation.FrameNavigationImpl
import com.alancamargo.snookerscore.ui.navigation.WebViewNavigationImpl
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameViewModel
import com.alancamargo.snookerscore.ui.viewmodel.webview.WebViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { (frames: List<UiFrame>) ->
        FrameViewModel(
            frames = frames,
            useCases = FrameViewModel.UseCases(
                getMatchSummaryUseCase = get(),
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
            logger = get()
        )
    }
    factory<WebViewNavigation> { WebViewNavigationImpl() }
    viewModel { WebViewViewModel() }
    factory<FrameNavigation> { FrameNavigationImpl() }
}