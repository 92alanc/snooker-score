package com.alancamargo.snookerscore.domain.usecase.player

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetPlayersUseCaseTest {

    private val mockRepository = mockk<PlayerRepository>()
    private val useCase = GetPlayersUseCase(mockRepository)

    @Test
    fun `invoke should return players`() = runBlocking {
        val expected = getPlayers()
        every { mockRepository.getPlayers() } returns flow { emit(expected) }

        val result = useCase.invoke()

        result.test {
            val players = awaitItem()
            assertThat(players).isEqualTo(expected)
            awaitComplete()
        }
    }

    private fun getPlayers() = listOf(
        Player(name = "Mark Selby"),
        Player(name = "Judd Trump"),
        Player(name = "Ronnie o\' Sullivan")
    )

}
