package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alancamargo.snookerscore.data.model.DbFrame

@Dao
interface FrameDao {

    @Insert(entity = DbFrame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateFrame(frame: DbFrame)

    @Query("SELECT * FROM Frames WHERE matchDateTime = :matchDateTime ORDER BY positionInMatch")
    suspend fun getFrames(matchDateTime: Long): List<DbFrame>

}
