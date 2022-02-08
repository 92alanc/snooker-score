package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Frames",
    foreignKeys = [
        ForeignKey(
            entity = DbScore::class,
            parentColumns = [ SCORE_COLUMN_ID ],
            childColumns = ["player1Score", "player2Score"]
        )
    ]
)
data class DbFrame(
    @PrimaryKey val id: String,
    val player1Score: DbScore,
    val player2Score: DbScore
)
