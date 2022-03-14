package com.alancamargo.snookerscore.features.playerstats.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.alancamargo.snookerscore.features.player.data.model.DbPlayer
import com.alancamargo.snookerscore.features.player.data.model.PLAYER_COLUMN_ID

@Entity(
    tableName = "PlayerStats",
    foreignKeys = [
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [PLAYER_COLUMN_ID],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbPlayerStats(
    @PrimaryKey val id: String,
    @ColumnInfo(index = true) val playerId: String,
    val matchesWon: Int,
    val highestScore: Int,
    val highestBreak: Int
)
