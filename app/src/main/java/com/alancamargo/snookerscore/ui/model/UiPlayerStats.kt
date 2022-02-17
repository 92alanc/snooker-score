package com.alancamargo.snookerscore.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiPlayerStats(
    val id: String,
    val player: UiPlayer,
    val matchesWon: Int = 0,
    val highestScore: Int = 0,
    val highestBreak: Int = 0
) : Parcelable
