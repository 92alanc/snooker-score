package com.alancamargo.snookerscore.features.frame.domain.model

sealed class Foul {

    data class WithObjectBall(val ball: Ball) : Foul()

    object Other : Foul()

}
