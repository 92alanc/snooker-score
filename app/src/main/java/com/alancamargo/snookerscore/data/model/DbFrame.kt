package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Frames",
    foreignKeys = [
        ForeignKey(
            entity = DbScore::class,
            parentColumns = [ SCORE_COLUMN_ID, SCORE_COLUMN_ID ],
            childColumns = [ "player1ScoreId", "player2ScoreId" ]
        )
    ]
)
data class DbFrame(
    @PrimaryKey val id: String,
    val player1ScoreId: String,
    val player2ScoreId: String
)
