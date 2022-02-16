package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alancamargo.snookerscore.data.model.DbPlayer

@Dao
interface PlayerDao {

    @Query("SELECT * FROM Players")
    suspend fun getPlayers(): List<DbPlayer>

    @Insert(entity = DbPlayer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdatePlayer(player: DbPlayer)

    @Query("DELETE FROM Players WHERE id = :playerId")
    suspend fun deletePlayer(playerId: String)

}