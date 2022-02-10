package com.alancamargo.snookerscore.domain

import com.alancamargo.snookerscore.data.repository.PlayerRepositoryImpl
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.domain.usecase.AddPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.DeletePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.GetPlayersUseCase
import com.alancamargo.snookerscore.domain.usecase.UpdatePlayerUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<PlayerRepository> { PlayerRepositoryImpl(localDataSource = get()) }
    factory { GetPlayersUseCase(repository = get()) }
    factory { AddPlayerUseCase(repository = get()) }
    factory { DeletePlayerUseCase(repository = get()) }
    factory { UpdatePlayerUseCase(repository = get()) }
    factory { GetPenaltyValueUseCase() }
}