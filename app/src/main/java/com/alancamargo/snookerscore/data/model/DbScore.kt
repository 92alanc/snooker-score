package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Scores",
    foreignKeys = [
        ForeignKey(
            entity = DbFrame::class,
            parentColumns = [FRAME_COLUMN_ID],
            childColumns = ["frameId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbScore(
    @PrimaryKey val id: String,
    val frameId: String,
    val player1Score: Int,
    val player2Score: Int,
    val player1HighestBreak: Int,
    val player2HighestBreak: Int
)
