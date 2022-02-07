package com.alancamargo.snookerscore.domain.usecase

import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul

private const val MIN_PENALTY = 4

class GetPenaltyValueUseCase {

    operator fun invoke(foul: Foul): Int = when (foul) {
        is Foul.BallOffTable -> handleBall(foul.ball)
        is Foul.HitNothing -> MIN_PENALTY
        is Foul.BallPotted -> handleBall(foul.ball)
        is Foul.BallPushed -> handlePushedBall(foul.ball)
    }

    private fun handleBall(ball: Ball): Int = when (ball) {
        Ball.CUE_BALL,
        Ball.RED,
        Ball.YELLOW,
        Ball.GREEN,
        Ball.BROWN -> MIN_PENALTY
        else -> ball.value
    }

    private fun handlePushedBall(ball: Ball): Int = when (ball) {
        Ball.CUE_BALL -> throw IllegalArgumentException("Push shots must involve an object ball")
        else -> handleBall(ball)
    }

}
