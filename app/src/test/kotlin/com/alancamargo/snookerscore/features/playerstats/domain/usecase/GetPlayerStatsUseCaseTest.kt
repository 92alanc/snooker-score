package com.alancamargo.snookerscore.features.playerstats.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.playerstats.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.testtools.getPlayerStats
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetPlayerStatsUseCaseTest {

    private val mockRepository = mockk<PlayerStatsRepository>()
    private val useCase = GetPlayerStatsUseCase(mockRepository)

    @Test
    fun `invoke should get player stats`() = runBlocking {
        val expected = getPlayerStats()
        every { mockRepository.getPlayerStats(expected.player) } returns flow { emit(expected) }

        val result = useCase.invoke(expected.player)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
