package com.alancamargo.snookerscore.data.local

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.PlayerDao
import com.alancamargo.snookerscore.data.db.PlayerStatsDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.data.mapping.toDomain
import com.alancamargo.snookerscore.data.model.DbPlayer
import com.alancamargo.snookerscore.domain.model.Player
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlayerLocalDataSourceImplTest {

    private val mockPlayerDao = mockk<PlayerDao>(relaxed = true)
    private val mockPlayerStatsDao = mockk<PlayerStatsDao>(relaxed = true)

    private val localDataSource = PlayerLocalDataSourceImpl(
        playerDao = mockPlayerDao,
        playerStatsDao = mockPlayerStatsDao
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
    fun `deletePlayer should remove player from player and player stats databases`() = runBlocking {
        val player = Player(id = "1111", name = "Brinquinho")

        val result = localDataSource.deletePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockPlayerDao.deletePlayer(player.id) }
        coVerify { mockPlayerStatsDao.deletePlayerStats(player.id) }
    }

    private fun getDbPlayerList() = listOf(
        DbPlayer(id = "12345", name = "Steve Davis"),
        DbPlayer(id = "54321", name = "Mark Selby")
    )

}
