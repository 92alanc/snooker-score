package com.alancamargo.snookerscore.domain.usecase.score

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.ScoreRepository
import com.alancamargo.snookerscore.testtools.getFrame
import com.alancamargo.snookerscore.testtools.getScore
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetScoreUseCaseTest {

    private val mockRepository = mockk<ScoreRepository>()
    private val useCase = GetScoreUseCase(mockRepository)

    @Test
    fun `invoke should return score`() = runBlocking {
        val frame = getFrame()
        val expected = getScore()
        every { mockRepository.getScore(frame) } returns flow { emit(expected) }

        val result = useCase.invoke(frame)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
