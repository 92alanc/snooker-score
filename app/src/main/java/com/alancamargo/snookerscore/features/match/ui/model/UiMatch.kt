package com.alancamargo.snookerscore.features.match.ui.model

import android.os.Parcelable
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiMatch(
    val dateTime: Long,
    val player1: UiPlayer,
    val player2: UiPlayer,
    val numberOfFrames: Int,
    val isFinished: Boolean
) : Parcelable
