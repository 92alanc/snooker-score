package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val SCORE_COLUMN_ID = "id"

@Entity(tableName = "Scores")
data class DbScore(
    @PrimaryKey val id: String,
    val score: Int,
    val highestBreak: Int
)
