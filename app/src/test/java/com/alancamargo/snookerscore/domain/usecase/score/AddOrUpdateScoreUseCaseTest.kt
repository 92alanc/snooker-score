package com.alancamargo.snookerscore.domain.usecase.score

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.ScoreRepository
import com.alancamargo.snookerscore.testtools.getScore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddOrUpdateScoreUseCaseTest {

    private val mockRepository = mockk<ScoreRepository>()
    private val useCase = AddOrUpdateScoreUseCase(mockRepository)

    @Test
    fun `invoke should add or update match`() = runBlocking {
        val score = getScore()
        every { mockRepository.addOrUpdateScore(score) } returns flow { emit(Unit) }

        val result = useCase.invoke(score)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
