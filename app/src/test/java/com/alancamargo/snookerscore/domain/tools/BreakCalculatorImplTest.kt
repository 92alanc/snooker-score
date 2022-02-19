package com.alancamargo.snookerscore.domain.tools

import com.alancamargo.snookerscore.domain.model.Ball
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BreakCalculatorImplTest {

    @Test
    fun `when some balls are potted getPoints should return the correct value`() {
        BreakCalculatorImpl().run {
            potBall(Ball.RED)
            potBall(Ball.BLACK)
            potBall(Ball.RED)

            assertThat(getPoints()).isEqualTo(9)
        }
    }

    @Test
    fun `when some balls are potted and some undone getPoints should return the correct value`() {
        BreakCalculatorImpl().run {
            potBall(Ball.RED)
            potBall(Ball.BLUE)
            potBall(Ball.RED)
            potBall(Ball.BROWN)

            undoLastPottedBall()
            undoLastPottedBall()

            assertThat(getPoints()).isEqualTo(6)
        }
    }

    @Test
    fun `clear should clear points`() {
        BreakCalculatorImpl().run {
            potBall(Ball.RED)
            potBall(Ball.GREEN)
            potBall(Ball.RED)
            potBall(Ball.PINK)

            clear()

            assertThat(getPoints()).isEqualTo(0)
        }
    }

}
