package com.alancamargo.snookerscore.features.player.ui.model

import androidx.annotation.DrawableRes
import com.alancamargo.snookerscore.R

enum class UiGender(@DrawableRes val iconRes: Int) {

    MALE(iconRes = R.drawable.ic_male),
    FEMALE(iconRes = R.drawable.ic_female)

}
