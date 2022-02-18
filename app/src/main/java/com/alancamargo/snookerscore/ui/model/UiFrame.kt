package com.alancamargo.snookerscore.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UiFrame(
    val id: String = UUID.randomUUID().toString(),
    val match: UiMatch
) : Parcelable
