package com.alancamargo.snookerscore.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
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
    fun `invoke should return all players`() = runBlocking {
        val expected = getPlayers()
        every { mockRepository.getPlayers() } returns flow { emit(expected) }

        val result = useCase.invoke()

        result.test {
            val players = awaitItem()
            assertThat(players).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `when repository throws exception invoke should return error`() = runBlocking {
        val errorMessage = "Something wrong happened"
        every { mockRepository.getPlayers() } returns flow { throw Throwable(errorMessage) }

        val result = useCase.invoke()

        result.test {
            val error = awaitError()
            assertThat(error).hasMessageThat().isEqualTo(errorMessage)
        }
    }

    private fun getPlayers() = listOf(
        Player(id = "1", name = "Mark Selby"),
        Player(id = "2", name = "Judd Trump"),
        Player(id = "3", name = "Ronnie o\' Sullivan")
    )

}
