package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val PLAYER_COLUMN_NAME = "name"

@Entity(tableName = "Players")
data class DbPlayer(@PrimaryKey val name: String)
