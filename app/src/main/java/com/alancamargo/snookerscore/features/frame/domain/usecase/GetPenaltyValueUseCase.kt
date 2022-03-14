package com.alancamargo.snookerscore.features.frame.domain.usecase

import com.alancamargo.snookerscore.features.frame.domain.model.Ball
import com.alancamargo.snookerscore.features.frame.domain.model.Foul

private const val MIN_PENALTY = 4

class GetPenaltyValueUseCase {

    operator fun invoke(foul: Foul): Int = when (foul) {
        is Foul.WithObjectBall -> handleBall(foul.ball)
        is Foul.Other -> MIN_PENALTY
    }

    private fun handleBall(ball: Ball): Int = when (ball) {
        Ball.RED,
        Ball.YELLOW,
        Ball.GREEN,
        Ball.BROWN -> MIN_PENALTY
        else -> ball.value
    }

}
