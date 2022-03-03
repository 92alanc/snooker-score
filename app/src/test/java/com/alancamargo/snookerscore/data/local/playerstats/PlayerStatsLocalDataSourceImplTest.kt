package com.alancamargo.snookerscore.data.local.playerstats

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getPlayer
import com.alancamargo.snookerscore.testtools.getPlayerStats
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlayerStatsLocalDataSourceImplTest {

    private val mockDatabase = mockk<PlayerStatsDao>(relaxed = true)
    private val localDataSource = PlayerStatsLocalDataSourceImpl(playerStatsDao = mockDatabase)

    @Test
    fun `getPlayerStats should return player stats from database`() = runBlocking {
        val playerStats = getPlayerStats()
        coEvery { mockDatabase.getPlayerStats(playerStats.player.id) } returns playerStats.toData()

        val result = localDataSource.getPlayerStats(playerStats.player)

        result.test {
            assertThat(awaitItem()).isEqualTo(playerStats)
            awaitComplete()
        }
    }

    @Test
    fun `when database throws exception getPlayerStats should return error`() = runBlocking {
        val player = getPlayer()
        coEvery { mockDatabase.getPlayerStats(player.id) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.getPlayerStats(player)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `addOrUpdatePlayerStats should add or update player stats on database`() = runBlocking {
        val playerStats = getPlayerStats()

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addOrUpdatePlayerStats(playerStats.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdatePlayerStats should return error`() = runBlocking {
        val playerStats = getPlayerStats()
        coEvery {
            mockDatabase.addOrUpdatePlayerStats(playerStats.toData())
        } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
