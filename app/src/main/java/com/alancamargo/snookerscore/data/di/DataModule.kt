package com.alancamargo.snookerscore.data.di

import androidx.room.Room
import com.alancamargo.snookerscore.data.db.provider.DatabaseProvider
import com.alancamargo.snookerscore.data.local.PlayerLocalDataSource
import com.alancamargo.snookerscore.data.local.PlayerLocalDataSourceImpl
import org.koin.android.ext.koin.androidContext
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
        PlayerLocalDataSourceImpl(
            playerDao = get<DatabaseProvider>().providePlayerDao(),
            playerStatsDao = get<DatabaseProvider>().providePlayerStatsDao()
        )
    }
}