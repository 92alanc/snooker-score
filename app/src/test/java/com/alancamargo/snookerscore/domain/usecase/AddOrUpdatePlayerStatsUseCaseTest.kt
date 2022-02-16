package com.alancamargo.snookerscore.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
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
        val playerStats = PlayerStats(
            player = Player(id = "999", name = "Buzz Buzzard"),
            matchesWon = 256,
            highestScore = 100,
            highestBreak = 100
        )
        every { mockRepository.addOrUpdatePlayerStats(playerStats) } returns flow { emit(Unit) }

        val result = useCase.invoke(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}