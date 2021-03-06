package com.alancamargo.snookerscore.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alancamargo.snookerscore.features.frame.data.db.FrameDao
import com.alancamargo.snookerscore.features.frame.data.model.DbFrame
import com.alancamargo.snookerscore.features.match.data.db.MatchDao
import com.alancamargo.snookerscore.features.match.data.model.DbMatch
import com.alancamargo.snookerscore.features.player.data.db.PlayerDao
import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.playerstats.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.features.playerstats.data.model.DbPlayerStats

@Database(
    entities = [
        DbPlayer::class,
        DbPlayerStats::class,
        DbMatch::class,
        DbFrame::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DatabaseProvider : RoomDatabase() {

    abstract fun providePlayerDao(): PlayerDao

    abstract fun providePlayerStatsDao(): PlayerStatsDao

    abstract fun provideFrameDao(): FrameDao

    abstract fun provideMatchDao(): MatchDao

}
