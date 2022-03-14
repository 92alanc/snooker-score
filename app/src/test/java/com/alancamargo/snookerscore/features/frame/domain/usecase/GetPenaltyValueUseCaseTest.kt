package com.alancamargo.snookerscore.features.frame.domain.usecase

import com.alancamargo.snookerscore.features.frame.domain.model.Ball
import com.alancamargo.snookerscore.features.frame.domain.model.Foul
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private const val MIN_PENALTY = 4

class GetPenaltyValueUseCaseTest {

    private val useCase = GetPenaltyValueUseCase()

    @Test
    fun `when red ball is involved invoke should return minimum penalty`() {
        val ball = Ball.RED
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when yellow ball is involved invoke should return minimum penalty`() {
        val ball = Ball.YELLOW
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when green ball is involved invoke should return minimum penalty`() {
        val ball = Ball.GREEN
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when brown ball is involved invoke should return minimum penalty`() {
        val ball = Ball.BROWN
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when blue ball is involved invoke should return ball value`() {
        val ball = Ball.BLUE
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when pink ball is involved invoke should return ball value`() {
        val ball = Ball.PINK
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when black ball is involved invoke should return ball value`() {
        val ball = Ball.BLACK
        val foul = Foul.WithObjectBall(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `with other kind of foul invoke should return minimum penalty`() {
        val foul = Foul.Other

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

}
