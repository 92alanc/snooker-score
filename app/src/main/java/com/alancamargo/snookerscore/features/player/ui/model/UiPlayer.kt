package com.alancamargo.snookerscore.features.player.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiPlayer(
    val id: String,
    val name: String,
    val gender: UiGender
) : Parcelable
