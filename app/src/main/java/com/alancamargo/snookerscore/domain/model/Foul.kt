package com.alancamargo.snookerscore.domain.model

sealed class Foul {

    data class WithObjectBall(val ball: Ball) : Foul()

    object Other : Foul()

}
