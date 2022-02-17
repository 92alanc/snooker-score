package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Scores",
    foreignKeys = [
        ForeignKey(
            entity = DbMatch::class,
            parentColumns = [MATCH_COLUMN_DATE_TIME],
            childColumns = ["matchDateTime"],
            onDelete = ForeignKey.CASCADE
        ),
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
    val matchDateTime: Long,
    val frameId: String,
    val score: Int,
    val highestBreak: Int
)
