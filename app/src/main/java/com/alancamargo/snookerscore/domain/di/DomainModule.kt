package com.alancamargo.snookerscore.domain.di

import com.alancamargo.snookerscore.data.repository.PlayerRepositoryImpl
import com.alancamargo.snookerscore.data.repository.PlayerStatsRepositoryImpl
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.domain.usecase.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.DeletePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.GetPlayersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<PlayerRepository> { PlayerRepositoryImpl(localDataSource = get()) }
    factory { GetPlayersUseCase(repository = get()) }
    factory { AddOrUpdatePlayerUseCase(repository = get()) }
    factory { DeletePlayerUseCase(repository = get()) }
    factory { GetPenaltyValueUseCase() }
    factory<PlayerStatsRepository> { PlayerStatsRepositoryImpl(localDataSource = get()) }
    factory { GetPlayerStatsUseCase(repository = get()) }
    factory { AddOrUpdatePlayerStatsUseCase(repository = get()) }
}