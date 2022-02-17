package com.alancamargo.snookerscore.domain.usecase.frame

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DeleteFrameUseCaseTest {

    private val mockRepository = mockk<FrameRepository>()
    private val useCase = DeleteFrameUseCase(mockRepository)

    @Test
    fun `invoke should delete frame`() = runBlocking {
        val player = Player(name = "Daffy Duck")
        val match = Match(player1 = player, player2 = player)
        val frame = Frame(match = match)
        every { mockRepository.deleteFrame(frame) } returns flow { emit(Unit) }

        val result = useCase.invoke(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
