package com.alancamargo.snookerscore.features.frame.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.data.db.provider.DatabaseProvider
import com.alancamargo.snookerscore.features.frame.data.local.FrameLocalDataSource
import com.alancamargo.snookerscore.features.frame.data.local.FrameLocalDataSourceImpl
import com.alancamargo.snookerscore.features.frame.data.repository.FrameRepositoryImpl
import com.alancamargo.snookerscore.features.frame.domain.repository.FrameRepository
import com.alancamargo.snookerscore.features.frame.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.features.frame.domain.tools.BreakCalculatorImpl
import com.alancamargo.snookerscore.features.frame.domain.usecase.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.features.frame.domain.usecase.GetFramesUseCase
import com.alancamargo.snookerscore.features.frame.domain.usecase.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.navigation.FrameNavigation
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.frame.ui.navigation.FrameNavigationImpl
import com.alancamargo.snookerscore.features.frame.ui.viewmodel.FrameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

class FrameModule : FeatureModule() {

    override val domain = module {
        factory<FrameRepository> { FrameRepositoryImpl(localDataSource = get()) }
        factory { AddOrUpdateFrameUseCase(repository = get()) }
        factory { GetFramesUseCase(repository = get()) }
        factory { GetPenaltyValueUseCase() }
        factory<BreakCalculator> { BreakCalculatorImpl() }
    }

    override val data = module {
        factory<FrameLocalDataSource> {
            FrameLocalDataSourceImpl(
                frameDao = getDatabaseProvider().provideFrameDao(),
                matchDao = getDatabaseProvider().provideMatchDao()
            )
        }
    }

    override val ui = module {
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
        factory<FrameNavigation> { FrameNavigationImpl() }
    }

}

private fun Scope.getDatabaseProvider() = get<DatabaseProvider>()
