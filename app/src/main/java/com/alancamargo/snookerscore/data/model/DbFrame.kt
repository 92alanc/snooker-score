package com.alancamargo.snookerscore.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.alancamargo.snookerscore.features.match.data.model.DbMatch
import com.alancamargo.snookerscore.features.match.data.model.MATCH_COLUMN_DATE_TIME

@Entity(
    tableName = "Frames",
    foreignKeys = [
        ForeignKey(
            entity = DbMatch::class,
            parentColumns = [MATCH_COLUMN_DATE_TIME],
            childColumns = ["matchDateTime"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbFrame(
    @PrimaryKey val id: String,
    val positionInMatch: Int,
    @ColumnInfo(index = true) val matchDateTime: Long,
    val player1Score: Int,
    val player2Score: Int,
    val player1HighestBreak: Int,
    val player2HighestBreak: Int,
    val isFinished: Boolean
)
