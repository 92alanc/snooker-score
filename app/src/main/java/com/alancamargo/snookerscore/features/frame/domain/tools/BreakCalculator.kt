package com.alancamargo.snookerscore.features.frame.domain.tools

import com.alancamargo.snookerscore.features.frame.domain.model.Ball

interface BreakCalculator {

    fun potBall(ball: Ball)

    fun undoLastPottedBall(): Ball

    fun getPoints(): Int

    fun clear()

}
