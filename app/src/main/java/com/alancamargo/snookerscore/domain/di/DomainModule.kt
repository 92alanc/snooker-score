package com.alancamargo.snookerscore.domain.di

import com.alancamargo.snookerscore.data.repository.FrameRepositoryImpl
import com.alancamargo.snookerscore.data.repository.MatchRepositoryImpl
import com.alancamargo.snookerscore.data.repository.PlayerRepositoryImpl
import com.alancamargo.snookerscore.data.repository.PlayerStatsRepositoryImpl
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.DeleteFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.match.AddOrUpdateMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DeletePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
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
    factory<FrameRepository> { FrameRepositoryImpl(localDataSource = get()) }
    factory { AddFrameUseCase(repository = get()) }
    factory { DeleteFrameUseCase(repository = get()) }
    factory<MatchRepository> { MatchRepositoryImpl(localDataSource = get()) }
    factory { AddOrUpdateMatchUseCase(repository = get()) }
    factory { DeleteMatchUseCase(repository = get()) }
}