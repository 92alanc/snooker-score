package com.alancamargo.snookerscore.data.db.provider

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alancamargo.snookerscore.data.db.FrameDao
import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.data.model.DbFrame
import com.alancamargo.snookerscore.data.model.DbMatch
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.data.model.DbScore

@Database(
    entities = [
        DbPlayer::class,
        DbPlayerStats::class,
        DbFrame::class ,
        DbMatch::class,
        DbScore::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseProvider : RoomDatabase() {

    abstract fun providePlayerDao(): PlayerDao

    abstract fun providePlayerStatsDao(): PlayerStatsDao

    abstract fun provideFrameDao(): FrameDao

}
