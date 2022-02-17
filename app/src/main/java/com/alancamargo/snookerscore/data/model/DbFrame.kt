package com.alancamargo.snookerscore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

const val FRAME_COLUMN_ID = "id"

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
    val matchDateTime: Long
)
