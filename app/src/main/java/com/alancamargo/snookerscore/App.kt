package com.alancamargo.snookerscore

import android.app.Application
import com.alancamargo.snookerscore.core.arch.di.KoinAppDeclarationProvider
import org.koin.core.context.GlobalContext.startKoin

@Suppress("Unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(KoinAppDeclarationProvider.provideAppDeclaration(app = this))
        //FirebaseApp.initializeApp(this)
    }

}
