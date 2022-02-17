package com.alancamargo.snookerscore.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.alancamargo.snookerscore.data.model.DbMatch

@Dao
interface MatchDao {

    @Insert(entity = DbMatch::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateMatch(match: DbMatch)

}
