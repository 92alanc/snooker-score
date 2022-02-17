package com.alancamargo.snookerscore.domain.tools

import com.alancamargo.snookerscore.domain.model.Ball
import java.util.Stack

class BreakCalculatorImpl : BreakCalculator {

    private var points = 0
    private val pottedBalls = Stack<Ball>()

    override fun potBall(ball: Ball) {
        pottedBalls.push(ball)
        points += ball.value
    }

    override fun undoLastPottedBall() {
        points -= pottedBalls.pop().value
    }

    override fun getPoints(): Int = points

}
