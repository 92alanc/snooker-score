package com.alancamargo.snookerscore.features.frame.domain.tools

import com.alancamargo.snookerscore.features.frame.domain.model.Ball
import java.util.Stack

class BreakCalculatorImpl : BreakCalculator {

    private var points = 0
    private val pottedBalls = Stack<Ball>()

    override fun potBall(ball: Ball) {
        pottedBalls.push(ball)
        points += ball.value
    }

    override fun undoLastPottedBall(): Ball {
        return pottedBalls.pop().also { lastPottedBall ->
            points -= lastPottedBall.value
        }
    }

    override fun getPoints(): Int = points

    override fun clear() {
        pottedBalls.clear()
        points = 0
    }

}
