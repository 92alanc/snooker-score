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
class AddOrUpdateMatchUseCaseTest {

    private val mockRepository = mockk<MatchRepository>()
    private val useCase = AddOrUpdateMatchUseCase(mockRepository)

    @Test
    fun `invoke should add or update match`() = runBlocking {
        val match = getMatch()
        every { mockRepository.addOrUpdateMatch(match) } returns flow { emit(Unit) }

        val result = useCase.invoke(match)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
