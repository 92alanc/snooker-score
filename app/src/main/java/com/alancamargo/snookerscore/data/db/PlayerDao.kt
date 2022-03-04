package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alancamargo.snookerscore.data.model.DbPlayer

@Dao
interface PlayerDao {

    @Query("SELECT * FROM Players ORDER BY name")
    suspend fun getPlayers(): List<DbPlayer>

    @Query("SELECT * FROM Players WHERE id = :playerId LIMIT 1")
    suspend fun getPlayer(playerId: String): DbPlayer?

    @Insert(entity = DbPlayer::class)
    suspend fun addPlayer(player: DbPlayer)

    @Update(entity = DbPlayer::class)
    suspend fun updatePlayer(player: DbPlayer)

    @Query("DELETE FROM Players WHERE id = :playerId")
    suspend fun deletePlayer(playerId: String)

}
