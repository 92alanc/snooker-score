package com.alancamargo.snookerscore.features.player.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.data.db.provider.DatabaseProvider
import com.alancamargo.snookerscore.features.player.data.local.PlayerLocalDataSource
import com.alancamargo.snookerscore.features.player.data.local.PlayerLocalDataSourceImpl
import com.alancamargo.snookerscore.features.player.data.repository.PlayerRepositoryImpl
import com.alancamargo.snookerscore.features.player.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.features.player.domain.usecase.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.DeletePlayerUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.GetPlayersUseCase
import com.alancamargo.snookerscore.features.player.ui.navigation.PlayerListNavigationImpl
import com.alancamargo.snookerscore.features.player.ui.viewmodel.PlayerListViewModel
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

class PlayerModule : FeatureModule() {

    override val domain = module {
        factory<PlayerRepository> { PlayerRepositoryImpl(localDataSource = get()) }
        factory { GetPlayersUseCase(repository = get()) }
        factory { AddOrUpdatePlayerUseCase(repository = get()) }
        factory { DeletePlayerUseCase(repository = get()) }
        factory { ArePlayersTheSameUseCase() }
    }

    override val data = module {
        factory<PlayerLocalDataSource> {
            PlayerLocalDataSourceImpl(playerDao = getDatabaseProvider().providePlayerDao())
        }
    }

    override val ui = module {
        viewModel { params ->
            PlayerListViewModel(
                isPickingPlayer = params.get(),
                addOrUpdatePlayerUseCase = get(),
                addOrUpdatePlayerStatsUseCase = get(),
                getPlayersUseCase = get(),
                preferenceManager = get(),
                logger = get()
            )
        }
        factory<PlayerListNavigation> { PlayerListNavigationImpl() }
    }

    private fun Scope.getDatabaseProvider() = get<DatabaseProvider>()

}
