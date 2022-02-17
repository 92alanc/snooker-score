package com.alancamargo.snookerscore.data.local.player

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.testtools.getDbPlayerList
import com.alancamargo.snookerscore.testtools.getPlayer
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlayerLocalDataSourceImplTest {

    private val mockDatabase = mockk<PlayerDao>(relaxed = true)
    private val localDataSource = PlayerLocalDataSourceImpl(playerDao = mockDatabase)

    @Test
    fun `getPlayers should return players from database as domain`() = runBlocking {
        val dbPlayerList = getDbPlayerList()
        coEvery { mockDatabase.getPlayers() } returns dbPlayerList

        val result = localDataSource.getPlayers()

        val expected = dbPlayerList.map { it.toDomain() }
        result.test {
            assertThat(awaitItem()).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `when database throws exception getPlayers should return error`() = runBlocking {
        val message = "Database error. Go figure it out"
        coEvery { mockDatabase.getPlayers() } throws IOException(message)

        val result = localDataSource.getPlayers()

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    @Test
    fun `addOrUpdatePlayer should add or update player on database`() = runBlocking {
        val player = getPlayer()

        val result = localDataSource.addOrUpdatePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addOrUpdatePlayer(player.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdatePlayer should return error`() = runBlocking {
        val player = getPlayer()

        val message = "Could not add or update player for some reason"
        coEvery { mockDatabase.addOrUpdatePlayer(player.toData()) } throws IOException(message)

        val result = localDataSource.addOrUpdatePlayer(player)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    @Test
    fun `deletePlayer should remove player from database`() = runBlocking {
        val player = getPlayer()
        val result = localDataSource.deletePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.deletePlayer(player.id) }
    }

    @Test
    fun `when player database throws exception deletePlayer should return error`() = runBlocking {
        val player = getPlayer()

        val message = "Could not delete player for some reason"
        coEvery { mockDatabase.deletePlayer(player.id) } throws IOException(message)

        val result = localDataSource.deletePlayer(player)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

}
