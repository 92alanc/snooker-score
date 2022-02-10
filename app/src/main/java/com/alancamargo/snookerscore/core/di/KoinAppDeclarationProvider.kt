package com.alancamargo.snookerscore.core.di

import android.app.Application
import com.alancamargo.snookerscore.data.di.dataModule
import com.alancamargo.snookerscore.domain.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

object KoinAppDeclarationProvider {

    fun provideAppDeclaration(app: Application): KoinAppDeclaration = {
        androidContext(app)
        loadKoinModules(getModules())
    }

    private fun getModules(): List<Module> = listOf(
        domainModule,
        dataModule
    )

}
