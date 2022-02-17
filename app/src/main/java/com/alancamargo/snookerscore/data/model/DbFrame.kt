package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Frames",
    foreignKeys = [
        ForeignKey(
            entity = DbMatch::class,
            parentColumns = [MATCH_COLUMN_DATE_TIME],
            childColumns = ["matchDateTime"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DbScore::class,
            parentColumns = [SCORE_COLUMN_ID],
            childColumns = ["player1ScoreId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DbScore::class,
            parentColumns = [SCORE_COLUMN_ID],
            childColumns = ["player2ScoreId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbFrame(
    @PrimaryKey val id: String,
    val matchDateTime: Long,
    val player1ScoreId: String,
    val player2ScoreId: String
)
