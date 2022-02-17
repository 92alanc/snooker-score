package com.alancamargo.snookerscore.domain.usecase.match

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import com.alancamargo.snookerscore.testtools.getMatch
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddMatchUseCaseTest {

    private val mockRepository = mockk<MatchRepository>()
    private val useCase = AddMatchUseCase(mockRepository)

    @Test
    fun `invoke should add match`() = runBlocking {
        val match = getMatch()
        every { mockRepository.addMatch(match) } returns flow { emit(Unit) }

        val result = useCase.invoke(match)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
