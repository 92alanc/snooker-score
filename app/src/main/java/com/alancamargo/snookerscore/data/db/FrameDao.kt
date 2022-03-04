package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alancamargo.snookerscore.data.model.DbFrame

@Dao
interface FrameDao {

    @Query("SELECT * FROM Frames WHERE id = :frameId LIMIT 1")
    suspend fun getFrame(frameId: String): DbFrame?

    @Insert(entity = DbFrame::class)
    suspend fun addFrame(frame: DbFrame)

    @Update(entity = DbFrame::class)
    suspend fun updateFrame(frame: DbFrame)

    @Query("SELECT * FROM Frames WHERE matchDateTime = :matchDateTime ORDER BY positionInMatch")
    suspend fun getFrames(matchDateTime: Long): List<DbFrame>

}
