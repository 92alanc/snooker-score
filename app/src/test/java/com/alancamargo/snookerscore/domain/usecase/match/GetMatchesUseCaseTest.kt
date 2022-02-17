package com.alancamargo.snookerscore.domain.usecase.match

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.MatchRepository
import com.alancamargo.snookerscore.testtools.getMatchList
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetMatchesUseCaseTest {

    private val mockRepository = mockk<MatchRepository>()
    private val useCase = GetMatchesUseCase(mockRepository)

    @Test
    fun `invoke should get matches`() = runBlocking {
        val expected = getMatchList()
        every { mockRepository.getMatches() } returns flow { emit(expected) }

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
