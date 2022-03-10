package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alancamargo.snookerscore.data.model.DbMatch

@Dao
interface MatchDao {

    @Insert(entity = DbMatch::class)
    suspend fun addMatch(match: DbMatch)

    @Delete(entity = DbMatch::class)
    suspend fun deleteMatch(match: DbMatch)

    @Query("SELECT * FROM Matches")
    suspend fun getMatches(): List<DbMatch>

    @Update(entity = DbMatch::class)
    suspend fun updateMatch(match: DbMatch)

}
