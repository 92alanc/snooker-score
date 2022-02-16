package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Matches")
data class DbMatch(
    @PrimaryKey val id: String,
    val frameIds: String
)
