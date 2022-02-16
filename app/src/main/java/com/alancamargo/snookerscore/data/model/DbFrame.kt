package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "Frames",
    foreignKeys = [
        ForeignKey(
            entity = DbScore::class,
            parentColumns = [ SCORE_COLUMN_ID ],
            childColumns = ["player1ScoreJson", "player2ScoreJson"]
        )
    ]
)
@Serializable
data class DbFrame(
    @PrimaryKey val id: String,
    val player1ScoreJson: String,
    val player2ScoreJson: String
)
