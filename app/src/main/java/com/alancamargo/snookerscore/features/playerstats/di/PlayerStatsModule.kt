package com.alancamargo.snookerscore.features.playerstats.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.core.data.db.DatabaseProvider
import com.alancamargo.snookerscore.features.playerstats.data.local.PlayerStatsLocalDataSource
import com.alancamargo.snookerscore.features.playerstats.data.local.PlayerStatsLocalDataSourceImpl
import com.alancamargo.snookerscore.features.playerstats.data.repository.PlayerStatsRepositoryImpl
import com.alancamargo.snookerscore.features.playerstats.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.UpdatePlayerStatsWithMatchResultUseCase
import com.alancamargo.snookerscore.features.playerstats.ui.navigation.PlayerStatsNavigationImpl
import com.alancamargo.snookerscore.features.playerstats.ui.viewmodel.PlayerStatsViewModel
import com.alancamargo.snookerscore.navigation.PlayerStatsNavigation
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

class PlayerStatsModule : FeatureModule() {

    override val domain = module {
        factory<PlayerStatsRepository> { PlayerStatsRepositoryImpl(localDataSource = get()) }
        factory { GetPlayerStatsUseCase(repository = get()) }
        factory { AddOrUpdatePlayerStatsUseCase(repository = get()) }
        factory { UpdatePlayerStatsWithMatchResultUseCase(repository = get()) }
    }

    override val data = module {
        factory<PlayerStatsLocalDataSource> {
            PlayerStatsLocalDataSourceImpl(
                playerStatsDao = getDatabaseProvider().providePlayerStatsDao()
            )
        }
    }

    override val ui = module {
        viewModel {
            PlayerStatsViewModel(
                getPlayerStatsUseCase = get(),
                deletePlayerUseCase = get(),
                logger = get()
            )
        }
        factory<PlayerStatsNavigation> { PlayerStatsNavigationImpl() }
    }

    private fun Scope.getDatabaseProvider() = get<DatabaseProvider>()

}
