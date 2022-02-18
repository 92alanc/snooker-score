package com.alancamargo.snookerscore.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UiFrame(
    val id: String = UUID.randomUUID().toString(),
    val match: UiMatch,
    var player1Score: Int,
    var player2Score: Int,
    var player1HighestBreak: Int,
    var player2HighestBreak: Int
) : Parcelable
