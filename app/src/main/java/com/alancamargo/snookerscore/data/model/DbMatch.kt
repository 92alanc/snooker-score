package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val MATCH_COLUMN_DATE_TIME = "dateTime"

@Entity(
    tableName = "Matches",
    foreignKeys = [
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [PLAYER_COLUMN_ID],
            childColumns = ["player1Id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [PLAYER_COLUMN_ID],
            childColumns = ["player2Id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbMatch(
    @PrimaryKey val dateTime: Long,
    val player1Id: String,
    val player1FinalScoreId: String,
    val player2Id: String,
    val player2FinalScoreId: String
)
