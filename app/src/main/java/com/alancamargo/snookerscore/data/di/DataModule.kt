package com.alancamargo.snookerscore.data.di

import androidx.room.Room
import com.alancamargo.snookerscore.data.db.provider.DatabaseProvider
import com.alancamargo.snookerscore.data.local.player.PlayerLocalDataSource
import com.alancamargo.snookerscore.data.local.player.PlayerLocalDataSourceImpl
import com.alancamargo.snookerscore.data.local.playerstats.PlayerStatsLocalDataSource
import com.alancamargo.snookerscore.data.local.playerstats.PlayerStatsLocalDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            DatabaseProvider::class.java,
            "database"
        ).fallbackToDestructiveMigration().build()
    }

    factory<PlayerLocalDataSource> {
        PlayerLocalDataSourceImpl(playerDao = getDatabaseProvider().providePlayerDao())
    }

    factory<PlayerStatsLocalDataSource> {
        PlayerStatsLocalDataSourceImpl(
            playerStatsDao = getDatabaseProvider().providePlayerStatsDao()
        )
    }
}

private fun Scope.getDatabaseProvider() = get<DatabaseProvider>()