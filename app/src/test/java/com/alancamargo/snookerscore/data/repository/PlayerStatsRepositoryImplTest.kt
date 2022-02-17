package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.playerstats.PlayerStatsLocalDataSource
import com.alancamargo.snookerscore.testtools.getPlayerStats
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
        val expected = getPlayerStats()
        every {
            mockLocalDataSource.getPlayerStats(expected.player)
        } returns flow { emit(expected) }

        val result = repository.getPlayerStats(expected.player)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `addOrUpdatePlayerStats should add or update player stats`() = runBlocking {
        val playerStats = getPlayerStats()
        every {
            mockLocalDataSource.addOrUpdatePlayerStats(playerStats)
        } returns flow { emit(Unit) }

        val result = repository.addOrUpdatePlayerStats(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
