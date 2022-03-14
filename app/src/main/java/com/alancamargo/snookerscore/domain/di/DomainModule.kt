package com.alancamargo.snookerscore.domain.di

import com.alancamargo.snookerscore.data.repository.FrameRepositoryImpl
import com.alancamargo.snookerscore.data.repository.MatchRepositoryImpl
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.tools.BreakCalculatorImpl
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.GetFramesUseCase
import com.alancamargo.snookerscore.domain.usecase.match.AddMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.match.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.domain.usecase.match.GetMatchesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetPenaltyValueUseCase() }
    factory<MatchRepository> { MatchRepositoryImpl(localDataSource = get()) }
    factory { AddMatchUseCase(repository = get()) }
    factory { DeleteMatchUseCase(repository = get()) }
    factory<FrameRepository> { FrameRepositoryImpl(localDataSource = get()) }
    factory { AddOrUpdateFrameUseCase(repository = get()) }
    factory { GetFramesUseCase(repository = get()) }
    factory { GetMatchesUseCase(repository = get()) }
    factory<BreakCalculator> { BreakCalculatorImpl() }
    factory { GetMatchSummaryUseCase() }
}