package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    @PrimaryKey val id: String,
    val player1Id: String,
    val player2Id: String,
    val frameIds: String
)
