package com.alancamargo.snookerscore

import android.app.Application
import com.alancamargo.snookerscore.core.arch.di.KoinAppDeclarationProvider
import com.smaato.sdk.core.Config
import com.smaato.sdk.core.SmaatoSdk
import com.smaato.sdk.core.log.LogLevel
import org.koin.core.context.GlobalContext.startKoin

@Suppress("Unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(KoinAppDeclarationProvider.provideAppDeclaration(app = this))
        initialiseSmaato()
    }

    private fun initialiseSmaato() {
        val config = Config.builder().setLogLevel(LogLevel.ERROR).build()
        SmaatoSdk.init(this, config, BuildConfig.SMAATO_PUBLISHER_ID)
    }

}
