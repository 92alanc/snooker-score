package com.alancamargo.snookerscore.features.playerstats.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.playerstats.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.testtools.getPlayerStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class UpdatePlayerStatsWithMatchResultUseCaseTest {

    private val mockRepository = mockk<PlayerStatsRepository>()
    private val useCase = UpdatePlayerStatsWithMatchResultUseCase(mockRepository)

    @Test
    fun `invoke should update player stats`() = runBlocking {
        every { mockRepository.addOrUpdatePlayerStats(any()) } returns flow { emit(Unit) }
        val playerStats = getPlayerStats()

        val result = useCase.invoke(playerStats)

        val expected = playerStats.copy(matchesWon = playerStats.matchesWon + 1)
        verify { mockRepository.addOrUpdatePlayerStats(expected) }

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
