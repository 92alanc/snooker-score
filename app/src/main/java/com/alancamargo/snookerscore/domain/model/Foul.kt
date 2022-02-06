package com.alancamargo.snookerscore.domain.model

sealed class Foul {

    data class BallOffTable(val ball: Ball) : Foul()

    object HitNothing : Foul()

    data class BallPotted(val ball: Ball) : Foul()

}
