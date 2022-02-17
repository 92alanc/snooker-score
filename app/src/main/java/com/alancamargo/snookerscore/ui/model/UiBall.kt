package com.alancamargo.snookerscore.ui.model

import androidx.annotation.ColorRes
import com.alancamargo.snookerscore.R

enum class UiBall(@ColorRes val colour: Int) {

    CUE_BALL(colour = R.color.white),
    RED(colour = R.color.red),
    YELLOW(colour = R.color.yellow),
    GREEN(colour = R.color.green),
    BROWN(colour = R.color.brown),
    BLUE(colour = R.color.blue),
    PINK(colour = R.color.pink),
    BLACK(colour = R.color.black)

}
