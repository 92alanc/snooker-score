package com.alancamargo.snookerscore.domain.usecase.player

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import com.alancamargo.snookerscore.testtools.getPlayer
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddOrUpdatePlayerUseCaseTest {

    private val mockPlayerRepository = mockk<PlayerRepository>()
    private val mockPlayerStatsRepository = mockk<PlayerStatsRepository>()
    private val useCase = AddOrUpdatePlayerUseCase(mockPlayerRepository, mockPlayerStatsRepository)

    @Test
    fun `invoke should add or update player`() = runBlocking {
        val player = getPlayer()
        every { mockPlayerRepository.addOrUpdatePlayer(player) } returns flow { emit(Unit) }
        every {
            mockPlayerStatsRepository.addOrUpdatePlayerStats(playerStats = any())
        } returns flow { emit(Unit) }

        val result = useCase.invoke(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
