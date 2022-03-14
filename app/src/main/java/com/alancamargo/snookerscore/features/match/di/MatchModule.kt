package com.alancamargo.snookerscore.features.match.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.core.data.db.DatabaseProvider
import com.alancamargo.snookerscore.features.match.data.local.MatchLocalDataSource
import com.alancamargo.snookerscore.features.match.data.local.MatchLocalDataSourceImpl
import com.alancamargo.snookerscore.features.match.data.repository.MatchRepositoryImpl
import com.alancamargo.snookerscore.features.match.domain.repository.MatchRepository
import com.alancamargo.snookerscore.features.match.domain.usecase.AddMatchUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.DeleteMatchUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchesUseCase
import com.alancamargo.snookerscore.features.match.ui.navigation.MatchDetailsNavigationImpl
import com.alancamargo.snookerscore.features.match.ui.navigation.MatchListNavigationImpl
import com.alancamargo.snookerscore.features.match.ui.navigation.MatchSummaryNavigationImpl
import com.alancamargo.snookerscore.features.match.ui.navigation.NewMatchNavigationImpl
import com.alancamargo.snookerscore.features.match.ui.viewmodel.details.MatchDetailsViewModel
import com.alancamargo.snookerscore.features.match.ui.viewmodel.list.MatchListViewModel
import com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch.NewMatchViewModel
import com.alancamargo.snookerscore.features.match.ui.viewmodel.summary.MatchSummaryViewModel
import com.alancamargo.snookerscore.navigation.MatchDetailsNavigation
import com.alancamargo.snookerscore.navigation.MatchListNavigation
import com.alancamargo.snookerscore.navigation.MatchSummaryNavigation
import com.alancamargo.snookerscore.navigation.NewMatchNavigation
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

class MatchModule : FeatureModule() {

    override val domain = module {
        factory<MatchRepository> { MatchRepositoryImpl(localDataSource = get()) }
        factory { AddMatchUseCase(repository = get()) }
        factory { DeleteMatchUseCase(repository = get()) }
        factory { GetMatchesUseCase(repository = get()) }
        factory { GetMatchSummaryUseCase() }
    }

    override val data = module {
        factory<MatchLocalDataSource> {
            MatchLocalDataSourceImpl(
                matchDao = getDatabaseProvider().provideMatchDao(),
                playerDao = getDatabaseProvider().providePlayerDao()
            )
        }
    }

    override val ui = module {
        viewModel { MatchListViewModel(getMatchesUseCase = get(), logger = get()) }
        viewModel {
            NewMatchViewModel(
                arePlayersTheSameUseCase = get(),
                addMatchUseCase = get(),
                logger = get()
            )
        }
        viewModel {
            MatchDetailsViewModel(
                getFramesUseCase = get(),
                deleteMatchUseCase = get(),
                getMatchSummaryUseCase = get(),
                logger = get()
            )
        }
        viewModel { (frames: List<UiFrame>) ->
            MatchSummaryViewModel(frames = frames, getMatchSummaryUseCase = get())
        }
        factory<MatchSummaryNavigation> { MatchSummaryNavigationImpl() }
        factory<MatchListNavigation> { MatchListNavigationImpl() }
        factory<NewMatchNavigation> { NewMatchNavigationImpl() }
        factory<MatchDetailsNavigation> { MatchDetailsNavigationImpl() }
    }

    private fun Scope.getDatabaseProvider() = get<DatabaseProvider>()

}
