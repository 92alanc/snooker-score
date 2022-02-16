package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PlayerStats",
    foreignKeys = [
        ForeignKey(
            entity = DbPlayer::class,
            parentColumns = [ PLAYER_COLUMN_ID ],
            childColumns = [ "playerId" ]
        )
    ]
)
data class DbPlayerStats(
    @PrimaryKey val id: String,
    val playerId: String,
    val matchesWon: Int,
    val highestScore: Int,
    val highestBreak: Int
)
