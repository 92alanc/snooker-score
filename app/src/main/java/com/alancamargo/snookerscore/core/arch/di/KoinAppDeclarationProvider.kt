package com.alancamargo.snookerscore.core.arch.di

import android.app.Application
import com.alancamargo.snookerscore.core.di.coreModule
import com.alancamargo.snookerscore.data.di.dataModule
import com.alancamargo.snookerscore.domain.di.domainModule
import com.alancamargo.snookerscore.features.main.di.MainModule
import com.alancamargo.snookerscore.features.playerstats.di.PlayerStatsModule
import com.alancamargo.snookerscore.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

object KoinAppDeclarationProvider {

    fun provideAppDeclaration(app: Application): KoinAppDeclaration = {
        androidContext(app)
        loadKoinModules(getModules())
        MainModule().load()
        PlayerStatsModule().load()
    }

    private fun getModules(): List<Module> = listOf(
        coreModule,
        domainModule,
        dataModule,
        uiModule
    )

}
