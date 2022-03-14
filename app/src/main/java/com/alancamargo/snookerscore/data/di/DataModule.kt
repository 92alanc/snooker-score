package com.alancamargo.snookerscore.data.di

import androidx.room.Room
import com.alancamargo.snookerscore.data.db.provider.DatabaseProvider
import com.alancamargo.snookerscore.data.local.frame.FrameLocalDataSource
import com.alancamargo.snookerscore.data.local.frame.FrameLocalDataSourceImpl
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

    factory<FrameLocalDataSource> {
        FrameLocalDataSourceImpl(
            frameDao = getDatabaseProvider().provideFrameDao(),
            matchDao = getDatabaseProvider().provideMatchDao()
        )
    }
}

private fun Scope.getDatabaseProvider() = get<DatabaseProvider>()