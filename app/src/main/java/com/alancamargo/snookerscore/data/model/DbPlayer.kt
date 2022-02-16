package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

const val PLAYER_COLUMN_ID = "id"

@Entity(tableName = "Players")
@Serializable
data class DbPlayer(
    @PrimaryKey val id: String,
    val name: String
)
