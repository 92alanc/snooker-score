package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PLAYER_COLUMN_ID = "id"

@Entity(tableName = "Players")
data class DbPlayer(
    @PrimaryKey val id: String,
    val name: String,
    val genderId: Int
)
