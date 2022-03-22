package com.alancamargo.snookerscore.core.arch.di

import android.app.Application
import com.alancamargo.snookerscore.core.di.CoreModule
import com.alancamargo.snookerscore.features.frame.di.FrameModule
import com.alancamargo.snookerscore.features.main.di.MainModule
import com.alancamargo.snookerscore.features.match.di.MatchModule
import com.alancamargo.snookerscore.features.player.di.PlayerModule
import com.alancamargo.snookerscore.features.playerstats.di.PlayerStatsModule
import com.alancamargo.snookerscore.features.webview.di.WebViewModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinAppDeclaration

object KoinAppDeclarationProvider {

    fun provideAppDeclaration(app: Application): KoinAppDeclaration = {
        androidContext(app)
        CoreModule().load()
        MainModule().load()
        PlayerStatsModule().load()
        PlayerModule().load()
        MatchModule().load()
        FrameModule().load()
        WebViewModule().load()
    }

}
