package com.alancamargo.snookerscore.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
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
        val player = Player(name = "Woody Woodpecker")
        val expected = PlayerStats(
            player = player,
            matchesWon = 72,
            highestScore = 147,
            highestBreak = 147
        )
        every { mockRepository.getPlayerStats(player) } returns flow { emit(expected) }

        val result = useCase.invoke(player)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
