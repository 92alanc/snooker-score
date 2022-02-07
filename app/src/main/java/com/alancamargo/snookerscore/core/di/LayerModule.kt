package com.alancamargo.snookerscore.core.di

import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

abstract class LayerModule {

    protected open val domainModule = module { }
    protected open val dataModule = module { }
    protected open val uiModule = module { }

    fun load() {
        loadKoinModules(listOf(domainModule, dataModule, uiModule))
    }

}
