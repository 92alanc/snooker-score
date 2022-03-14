package com.alancamargo.snookerscore.features.playerstats.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alancamargo.snookerscore.data.model.DbPlayerStats

@Dao
interface PlayerStatsDao {

    @Query("SELECT * FROM PlayerStats WHERE playerId = :playerId LIMIT 1")
    suspend fun getPlayerStats(playerId: String): DbPlayerStats?

    @Insert(entity = DbPlayerStats::class)
    suspend fun addPlayerStats(playerStats: DbPlayerStats)

    @Update(entity = DbPlayerStats::class)
    suspend fun updatePlayerStats(playerStats: DbPlayerStats)

    @Query("DELETE FROM PlayerStats WHERE playerId = :playerId")
    suspend fun deletePlayerStats(playerId: String)

}
