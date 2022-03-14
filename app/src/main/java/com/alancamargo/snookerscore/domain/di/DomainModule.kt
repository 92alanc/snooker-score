package com.alancamargo.snookerscore.domain.di

import com.alancamargo.snookerscore.data.repository.FrameRepositoryImpl
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.tools.BreakCalculatorImpl
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.GetFramesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetPenaltyValueUseCase() }
    factory<FrameRepository> { FrameRepositoryImpl(localDataSource = get()) }
    factory { AddOrUpdateFrameUseCase(repository = get()) }
    factory { GetFramesUseCase(repository = get()) }
    factory<BreakCalculator> { BreakCalculatorImpl() }
}