package com.alancamargo.snookerscore.ui.model

import androidx.annotation.ColorRes
import com.alancamargo.snookerscore.R

enum class UiBall(@ColorRes val colour: Int) {

    CUE_BALL(colour = R.color.ball_white),
    RED(colour = R.color.ball_red),
    YELLOW(colour = R.color.ball_yellow),
    GREEN(colour = R.color.ball_green),
    BROWN(colour = R.color.ball_brown),
    BLUE(colour = R.color.ball_blue),
    PINK(colour = R.color.ball_pink),
    BLACK(colour = R.color.ball_black)

}
