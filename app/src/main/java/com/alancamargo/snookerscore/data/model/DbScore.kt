package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

const val SCORE_COLUMN_ID = "id"

@Entity(
    tableName = "Scores",
    foreignKeys = [
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [ PLAYER_COLUMN_ID ],
            childColumns = [ "playerJson" ]
        )
    ]
)
@Serializable
data class DbScore(
    @PrimaryKey val id: String,
    val playerJson: String,
    val score: Int,
    val highestBreak: Int
)
