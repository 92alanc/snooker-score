package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alancamargo.snookerscore.data.model.DbPlayerStats

@Dao
interface PlayerStatsDao {

    @Query("SELECT * FROM PlayerStats WHERE playerJson = :playerJson LIMIT 1")
    suspend fun getPlayerStats(playerJson: String): DbPlayerStats

    @Insert(entity = DbPlayerStats::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePlayerStats(playerStats: DbPlayerStats)

    @Query("DELETE FROM PlayerStats WHERE playerJson = :playerJson")
    suspend fun deletePlayerStats(playerJson: String)

}
