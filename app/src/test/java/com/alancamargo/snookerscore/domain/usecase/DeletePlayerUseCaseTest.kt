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
class DeletePlayerUseCaseTest {

    private val mockRepository = mockk<PlayerRepository>()
    private val useCase = DeletePlayerUseCase(mockRepository)

    @Test
    fun `invoke should delete player`() = runBlocking {
        val player = Player(id = "1", name = "Neil Robertson")
        every { mockRepository.deletePlayer(player) } returns flow { emit(Unit) }

        val result = useCase.invoke(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `when repository throws exception invoke should return error`() = runBlocking {
        val player = Player(id = "2", name = "Yan Bingtao")
        val errorMessage = "Something wrong happened"
        every { mockRepository.deletePlayer(player) } returns flow { throw Throwable(errorMessage) }

        val result = useCase.invoke(player)

        result.test {
            val error = awaitError()
            assertThat(error).hasMessageThat().isEqualTo(errorMessage)
        }
    }

}
