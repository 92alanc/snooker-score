package com.alancamargo.snookerscore.domain.tools

import com.alancamargo.snookerscore.domain.model.Ball

interface BreakCalculator {

    fun potBall(ball: Ball)

    fun undoLastPottedBall(): Ball

    fun getPoints(): Int

    fun clear()

}
