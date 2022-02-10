package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alancamargo.snookerscore.data.model.DbPlayer

@Dao
interface PlayerDao {

    @Query("SELECT * FROM Players")
    suspend fun getPlayers(): List<DbPlayer>

    @Insert(entity = DbPlayer::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun addPlayer(player: DbPlayer)

    @Delete(entity = DbPlayer::class)
    suspend fun deletePlayer(player: DbPlayer)

    @Update(entity = DbPlayer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayer(player: DbPlayer)

}
