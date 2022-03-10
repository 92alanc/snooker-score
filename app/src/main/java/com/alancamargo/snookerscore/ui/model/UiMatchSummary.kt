package com.alancamargo.snookerscore.ui.model

import android.os.Parcelable
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
