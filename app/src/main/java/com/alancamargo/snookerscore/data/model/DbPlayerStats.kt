package com.alancamargo.snookerscore.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PlayerStats",
    foreignKeys = [
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [PLAYER_COLUMN_NAME],
            childColumns = ["playerName"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbPlayerStats(
    @PrimaryKey val id: String,
    @ColumnInfo(index = true) val playerName: String,
    val matchesWon: Int,
    val highestScore: Int,
    val highestBreak: Int
)
