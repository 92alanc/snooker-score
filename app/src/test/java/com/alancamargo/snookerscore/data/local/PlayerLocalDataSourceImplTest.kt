package com.alancamargo.snookerscore.data.local

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.domain.model.Player
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlayerLocalDataSourceImplTest {

    private val mockPlayerDao = mockk<PlayerDao>(relaxed = true)
    private val mockPlayerStatsLocalDataSource = mockk<PlayerStatsLocalDataSource>()

    private val localDataSource = PlayerLocalDataSourceImpl(
        playerDao = mockPlayerDao,
        playerStatsLocalDataSource = mockPlayerStatsLocalDataSource
    )

    @Test
    fun `getPlayers should return players from database as domain`() = runBlocking {
        val dbPlayerList = getDbPlayerList()
        coEvery { mockPlayerDao.getPlayers() } returns dbPlayerList

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
        coEvery { mockPlayerDao.getPlayers() } throws IOException(message)

        val result = localDataSource.getPlayers()

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    @Test
    fun `addOrUpdatePlayer should add or update player on database`() = runBlocking {
        val player = Player(id = "0000", name = "Baianinho de Mauá")

        val result = localDataSource.addOrUpdatePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockPlayerDao.addOrUpdatePlayer(player.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdatePlayer should return error`() = runBlocking {
        val message = "Could not add or update player for some reason"
        val player = DbPlayer(id = "0000", name = "Baianinho de Mauá")
        coEvery { mockPlayerDao.addOrUpdatePlayer(player) } throws IOException(message)

        val result = localDataSource.addOrUpdatePlayer(player.toDomain())

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    @Test
    fun `deletePlayer should remove player from player and player stats databases`() = runBlocking {
        val player = Player(id = "1111", name = "Brinquinho")
        every {
            mockPlayerStatsLocalDataSource.deletePlayerStats(player)
        } returns flow { emit(Unit) }

        val result = localDataSource.deletePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }

        verify { mockPlayerStatsLocalDataSource.deletePlayerStats(player) }
        coVerify { mockPlayerDao.deletePlayer(player.id) }
    }

    @Test
    fun `when player database throws exception deletePlayer should return error`() = runBlocking {
        val message = "Could not delete player for some reason"
        val player = Player(id = "1111", name = "Brinquinho")
        every {
            mockPlayerStatsLocalDataSource.deletePlayerStats(player)
        } returns flow { emit(Unit) }
        coEvery { mockPlayerDao.deletePlayer(player.id) } throws IOException(message)

        val result = localDataSource.deletePlayer(player)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    @Test
    fun `when player stats database throws exception deletePlayer should return error`() = runBlocking {
        val message = "Could not delete player for some reason"
        val player = Player(id = "1111", name = "Brinquinho")
        every {
            mockPlayerStatsLocalDataSource.deletePlayerStats(player)
        } returns flow { throw IOException(message) }
        coEvery { mockPlayerDao.deletePlayer(player.id) } returns Unit

        val result = localDataSource.deletePlayer(player)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

    private fun getDbPlayerList() = listOf(
        DbPlayer(id = "12345", name = "Steve Davis"),
        DbPlayer(id = "54321", name = "Mark Selby")
    )

}
