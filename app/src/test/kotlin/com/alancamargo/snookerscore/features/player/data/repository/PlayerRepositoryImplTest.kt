package com.alancamargo.snookerscore.features.player.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.player.data.local.PlayerLocalDataSource
import com.alancamargo.snookerscore.testtools.getPlayerList
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PlayerRepositoryImplTest {

    private val mockLocalDataSource = mockk<PlayerLocalDataSource>()
    private val repository = PlayerRepositoryImpl(mockLocalDataSource)

    @Test
    fun `getPlayers should return players`() = runBlocking {
        val expected = getPlayerList()
        every { mockLocalDataSource.getPlayers() } returns flow { emit(expected) }

        val result = repository.getPlayers()

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `addOrUpdatePlayer should add or update player on local data source`() = runBlocking {
        val player = getPlayerList().first()
        every { mockLocalDataSource.addOrUpdatePlayer(player) } returns flow { emit(Unit) }

        val result = repository.addOrUpdatePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `deletePlayer should delete player on local data source`() = runBlocking {
        val player = getPlayerList().last()
        every { mockLocalDataSource.deletePlayer(player) } returns flow { emit(Unit) }

        val result = repository.deletePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
