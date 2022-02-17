package com.alancamargo.snookerscore.domain.usecase.playerstats

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.testtools.getPlayerStats
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddOrUpdatePlayerStatsUseCaseTest {

    private val mockRepository = mockk<PlayerStatsRepository>()
    private val useCase = AddOrUpdatePlayerStatsUseCase(mockRepository)

    @Test
    fun `invoke should add or update player stats`() = runBlocking {
        val playerStats = getPlayerStats()
        every { mockRepository.addOrUpdatePlayerStats(playerStats) } returns flow { emit(Unit) }

        val result = useCase.invoke(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}