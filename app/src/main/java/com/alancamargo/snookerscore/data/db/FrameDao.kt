package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alancamargo.snookerscore.data.model.DbFrame

@Dao
interface FrameDao {

    @Insert(entity = DbFrame::class)
    suspend fun addFrame(frame: DbFrame)

    @Query("DELETE FROM Frames WHERE id = :frameId")
    suspend fun deleteFrame(frameId: String)

}
