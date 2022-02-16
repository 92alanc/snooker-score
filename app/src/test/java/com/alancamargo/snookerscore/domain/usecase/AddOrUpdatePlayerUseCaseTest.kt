package com.alancamargo.snookerscore.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddOrUpdatePlayerUseCaseTest {

    private val mockRepository = mockk<PlayerRepository>()
    private val useCase = AddOrUpdatePlayerUseCase(mockRepository)

    @Test
    fun `invoke should add or update player`() = runBlocking {
        val player = Player(name = "Mark Selby")
        every { mockRepository.addOrUpdatePlayer(player) } returns flow { emit(Unit) }

        val result = useCase.invoke(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
