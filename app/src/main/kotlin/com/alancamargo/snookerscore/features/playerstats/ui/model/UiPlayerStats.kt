package com.alancamargo.snookerscore.features.playerstats.ui.model

import android.os.Parcelable
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UiPlayerStats(
    val id: String = UUID.randomUUID().toString(),
    val player: UiPlayer,
    val matchesWon: Int = 0,
    val highestScore: Int = 0,
    val highestBreak: Int = 0
) : Parcelable
