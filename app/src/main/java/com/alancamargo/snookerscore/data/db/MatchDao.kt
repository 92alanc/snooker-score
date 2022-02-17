package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alancamargo.snookerscore.data.model.DbMatch

@Dao
interface MatchDao {

    @Insert(entity = DbMatch::class)
    suspend fun addMatch(match: DbMatch)

    @Query("DELETE FROM Matches WHERE dateTime = :matchDateTime")
    suspend fun deleteMatch(matchDateTime: Long)

    @Query("SELECT * FROM Matches")
    suspend fun getMatches(): List<DbMatch>

}
