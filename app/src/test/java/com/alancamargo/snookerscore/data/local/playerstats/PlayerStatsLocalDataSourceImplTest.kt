package com.alancamargo.snookerscore.data.local.playerstats

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.data.model.DbPlayerStats
import com.alancamargo.snookerscore.domain.model.Player
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

private const val PLAYER_ID = "456"

@ExperimentalTime
class PlayerStatsLocalDataSourceImplTest {

    private val mockDatabase = mockk<PlayerStatsDao>(relaxed = true)
    private val localDataSource = PlayerStatsLocalDataSourceImpl(playerStatsDao = mockDatabase)

    @Test
    fun `getPlayerStats should return player stats from database`() = runBlocking {
        val dbPlayerStats = getDbPlayerStats()
        coEvery { mockDatabase.getPlayerStats(PLAYER_ID) } returns dbPlayerStats
        val player = Player(id = PLAYER_ID, name = "Mark Selby")

        val result = localDataSource.getPlayerStats(player)

        result.test {
            assertThat(awaitItem()).isEqualTo(dbPlayerStats.toDomain(player))
            awaitComplete()
        }
    }

    @Test
    fun `when database throws exception getPlayerStats should return error`() = runBlocking {
        val message = "Database not working"
        coEvery { mockDatabase.getPlayerStats(PLAYER_ID) } throws IOException(message)

        val player = Player(id = PLAYER_ID, name = "Judd Trump")

        val result = localDataSource.getPlayerStats(player)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    @Test
    fun `addOrUpdatePlayerStats should add or update player stats on database`() = runBlocking {
        val player = Player(id = PLAYER_ID, name = "Ronnie o\' Sullivan")
        val expected = getDbPlayerStats()
        val playerStats = expected.toDomain(player)

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addOrUpdatePlayerStats(expected) }
    }

    @Test
    fun `when database throws exception addOrUpdatePlayerStats should return error`() = runBlocking {
        val player = Player(id = PLAYER_ID, name = "Kyren Wilson")
        val dbPlayerStats = getDbPlayerStats()
        val playerStats = dbPlayerStats.toDomain(player)

        val message = "This database sucks"
        coEvery { mockDatabase.addOrUpdatePlayerStats(dbPlayerStats) } throws IOException(message)

        val result = localDataSource.addOrUpdatePlayerStats(playerStats)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    private fun getDbPlayerStats() = DbPlayerStats(
        id = "123",
        playerId = PLAYER_ID,
        matchesWon = 13,
        highestScore = 147,
        highestBreak = 147
    )

}
