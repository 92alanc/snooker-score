package com.alancamargo.snookerscore.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiMatch(
    val dateTime: Long = System.currentTimeMillis(),
    val player1: UiPlayer,
    val player2: UiPlayer,
    val numberOfFrames: Int
) : Parcelable
