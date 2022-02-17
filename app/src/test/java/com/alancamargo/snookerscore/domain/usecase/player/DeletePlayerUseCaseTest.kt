package com.alancamargo.snookerscore.domain.usecase.player

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.testtools.getPlayer
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
        val player = getPlayer()
        every { mockRepository.deletePlayer(player) } returns flow { emit(Unit) }

        val result = useCase.invoke(player)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
