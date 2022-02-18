package com.alancamargo.snookerscore.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val MATCH_COLUMN_DATE_TIME = "dateTime"

@Entity(
    tableName = "Matches",
    foreignKeys = [
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [PLAYER_COLUMN_NAME],
            childColumns = ["player1Name"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [PLAYER_COLUMN_NAME],
            childColumns = ["player2Name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbMatch(
    @PrimaryKey val dateTime: Long,
    @ColumnInfo(index = true) val player1Name: String,
    @ColumnInfo(index = true) val player2Name: String,
    val numberOfFrames: Int
)
