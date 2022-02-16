package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.player.PlayerLocalDataSource
import com.alancamargo.snookerscore.domain.model.Player
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
        val expected = getPlayers()
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
        val player = getPlayers().first()
        every { mockLocalDataSource.addOrUpdatePlayer(player) } returns flow { emit(Unit) }

        val result = repository.addOrUpdatePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `deletePlayer should delete player on local data source`() = runBlocking {
        val player = getPlayers().last()
        every { mockLocalDataSource.deletePlayer(player) } returns flow { emit(Unit) }

        val result = repository.deletePlayer(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    private fun getPlayers() = listOf(
        Player(name = "Noel"),
        Player(name = "Cleitinho")
    )

}
