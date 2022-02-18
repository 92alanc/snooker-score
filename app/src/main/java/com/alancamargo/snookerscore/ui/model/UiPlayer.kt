package com.alancamargo.snookerscore.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UiPlayer(
    val id: String = UUID.randomUUID().toString(),
    val name: String
) : Parcelable
