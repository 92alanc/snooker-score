package com.alancamargo.snookerscore.domain.usecase

import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private const val MIN_PENALTY = 4

class GetPenaltyValueUseCaseTest {

    private val useCase = GetPenaltyValueUseCase()

    @Test
    fun `when nothing is hit invoke should return minimum penalty`() {
        val foul = Foul.HitNothing

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when cue ball flies off the table invoke should return minimum penalty`() {
        val ball = Ball.CUE_BALL
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when red ball flies off the table invoke should return minimum penalty`() {
        val ball = Ball.RED
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when yellow ball flies off the table invoke should return minimum penalty`() {
        val ball = Ball.YELLOW
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when green ball flies off the table invoke should return minimum penalty`() {
        val ball = Ball.GREEN
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when brown ball flies off the table invoke should return minimum penalty`() {
        val ball = Ball.BROWN
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when blue ball flies off the table invoke should return ball value`() {
        val ball = Ball.BLUE
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when pink ball flies off the table invoke should return ball value`() {
        val ball = Ball.PINK
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when black ball flies off the table invoke should return ball value`() {
        val ball = Ball.BLACK
        val foul = Foul.BallOffTable(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when cue ball is potted invoke should return minimum penalty`() {
        val ball = Ball.CUE_BALL
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when red ball is potted invoke should return minimum penalty`() {
        val ball = Ball.RED
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when yellow ball is potted invoke should return minimum penalty`() {
        val ball = Ball.YELLOW
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when green ball is potted invoke should return minimum penalty`() {
        val ball = Ball.GREEN
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when brown ball is potted invoke should return minimum penalty`() {
        val ball = Ball.BROWN
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(MIN_PENALTY)
    }

    @Test
    fun `when blue ball is potted invoke should return ball value`() {
        val ball = Ball.BLUE
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when pink ball is potted invoke should return ball value`() {
        val ball = Ball.PINK
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

    @Test
    fun `when black ball is potted invoke should return ball value`() {
        val ball = Ball.BLACK
        val foul = Foul.BallPotted(ball)

        val penalty = useCase.invoke(foul)

        assertThat(penalty).isEqualTo(ball.value)
    }

}
