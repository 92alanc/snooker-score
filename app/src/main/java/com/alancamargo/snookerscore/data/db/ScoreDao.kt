package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alancamargo.snookerscore.data.model.DbScore

@Dao
interface ScoreDao {

    @Insert(entity = DbScore::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateScore(score: DbScore)

    @Query("SELECT * FROM Scores WHERE frameId = :frameId LIMIT 1")
    suspend fun getScore(frameId: String): DbScore

}
