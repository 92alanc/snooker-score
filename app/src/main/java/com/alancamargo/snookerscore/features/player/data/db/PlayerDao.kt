package com.alancamargo.snookerscore.features.player.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alancamargo.snookerscore.features.player.data.model.DbPlayer

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

    @Delete(entity = DbPlayer::class)
    suspend fun deletePlayer(player: DbPlayer)

}
