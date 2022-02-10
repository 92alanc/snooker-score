package com.alancamargo.snookerscore.data.db.provider

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.model.DbPlayer

@Database(
    entities = [DbPlayer::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseProvider : RoomDatabase() {

    abstract fun providePlayerDao(): PlayerDao

}
