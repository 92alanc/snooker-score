package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.PlayerStatsLocalDataSource
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlayerStatsRepositoryImplTest {

    private val mockLocalDataSource = mockk<PlayerStatsLocalDataSource>()
    private val repository = PlayerStatsRepositoryImpl(mockLocalDataSource)

    @Test
    fun `getPlayerStats should return player stats`() = runBlocking {
        val player = Player(id = "123", name = "Willy E. Coyote")
        val expected = getPlayerStats(player)
        every { mockLocalDataSource.getPlayerStats(player) } returns flow { emit(expected) }

        val result = repository.getPlayerStats(player)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `addOrUpdatePlayerStats should add or update player stats`() = runBlocking {
        val player = Player(id = "456", name = "Road Runner")
        val playerStats = getPlayerStats(player)
        every {
            mockLocalDataSource.addOrUpdatePlayerStats(playerStats)
        } returns flow { emit(Unit) }

        val result = repository.addOrUpdatePlayerStats(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    private fun getPlayerStats(player: Player) = PlayerStats(
        player = player,
        matchesWon = 4,
        highestScore = 42,
        highestBreak = 8
    )

}
