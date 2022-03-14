package com.alancamargo.snookerscore.features.match.ui.model

import android.os.Parcelable
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiMatchSummary(
    val match: UiMatch,
    val winner: UiPlayer,
    val player1Score: Int,
    val player2Score: Int,
    val player1HighestBreak: Int,
    val player2HighestBreak: Int
) : Parcelable
