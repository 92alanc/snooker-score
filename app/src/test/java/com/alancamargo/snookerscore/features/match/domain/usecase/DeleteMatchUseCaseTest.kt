package com.alancamargo.snookerscore.features.match.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.match.domain.repository.MatchRepository
import com.alancamargo.snookerscore.testtools.getMatch
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DeleteMatchUseCaseTest {

    private val mockRepository = mockk<MatchRepository>()
    private val useCase = DeleteMatchUseCase(mockRepository)

    @Test
    fun `invoke should delete match`() = runBlocking {
        val match = getMatch()
        every { mockRepository.deleteMatch(match) } returns flow { emit(Unit) }

        val result = useCase.invoke(match)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
