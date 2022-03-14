package com.alancamargo.snookerscore.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.player.data.model.PLAYER_COLUMN_ID

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
    @ColumnInfo(index = true) val player1Id: String,
    @ColumnInfo(index = true) val player2Id: String,
    val numberOfFrames: Int,
    val isFinished: Boolean
)
