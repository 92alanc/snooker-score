package com.alancamargo.snookerscore.features.frame.ui.model

import android.os.Parcelable
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UiFrame(
    val id: String = UUID.randomUUID().toString(),
    val positionInMatch: Int,
    val match: UiMatch,
    var player1Score: Int,
    var player2Score: Int,
    var player1HighestBreak: Int,
    var player2HighestBreak: Int,
    var isFinished: Boolean
) : Parcelable
