package com.alancamargo.snookerscore.features.playerstats.data.local

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.features.playerstats.data.db.PlayerStatsDao
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
    fun `when player stats do not exist addOrUpdatePlayerStats should add player stats to database`() = runBlocking {
        val playerStats = getPlayerStats()
        coEvery { mockDatabase.getPlayerStats(playerStats.player.id) } returns null

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addPlayerStats(playerStats.toData()) }
    }

    @Test
    fun `when player stats exist addOrUpdatePlayerStats should update player stats on database`() = runBlocking {
        val playerStats = getPlayerStats()
        coEvery { mockDatabase.getPlayerStats(playerStats.player.id) } returns playerStats.toData()

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.updatePlayerStats(playerStats.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdatePlayerStats should return error`() = runBlocking {
        val playerStats = getPlayerStats()
        coEvery { mockDatabase.getPlayerStats(playerStats.player.id) } returns null
        coEvery {
            mockDatabase.addPlayerStats(playerStats.toData())
        } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
