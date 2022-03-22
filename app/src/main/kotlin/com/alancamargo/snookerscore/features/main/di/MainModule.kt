package com.alancamargo.snookerscore.features.main.di

import com.alancamargo.snookerscore.core.arch.di.FeatureModule
import com.alancamargo.snookerscore.features.main.data.analytics.MainAnalytics
import com.alancamargo.snookerscore.features.main.data.analytics.MainAnalyticsImpl
import com.alancamargo.snookerscore.features.main.data.remote.RulesUrlRemoteDataSource
import com.alancamargo.snookerscore.features.main.data.remote.RulesUrlRemoteDataSourceImpl
import com.alancamargo.snookerscore.features.main.data.repository.RulesUrlRepositoryImpl
import com.alancamargo.snookerscore.features.main.domain.repository.RulesUrlRepository
import com.alancamargo.snookerscore.features.main.domain.usecase.GetRulesUrlUseCase
import com.alancamargo.snookerscore.features.main.ui.navigation.MainNavigationImpl
import com.alancamargo.snookerscore.features.main.ui.viewmodel.MainViewModel
import com.alancamargo.snookerscore.navigation.MainNavigation
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MainModule : FeatureModule() {

    override val domain = module {
        factory { GetRulesUrlUseCase(repository = get()) }
        factory<RulesUrlRepository> { RulesUrlRepositoryImpl(remoteDataSource = get()) }
    }

    override val data = module {
        factory<RulesUrlRemoteDataSource> {
            RulesUrlRemoteDataSourceImpl(remoteConfigManager = get())
        }
        factory<MainAnalytics> { MainAnalyticsImpl(analytics = get()) }
    }

    override val ui = module {
        viewModel { MainViewModel(getRulesUrlUseCase = get(), analytics = get()) }
        factory<MainNavigation> { MainNavigationImpl() }
    }

}
