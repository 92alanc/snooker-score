package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.frame.FrameLocalDataSource
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class FrameRepositoryImplTest {

    private val mockLocalDataSource = mockk<FrameLocalDataSource>()
    private val repository = FrameRepositoryImpl(mockLocalDataSource)

    @Test
    fun `addFrame should add frame`() = runBlocking {
        val frame = getFrame()
        every { mockLocalDataSource.addFrame(frame) } returns flow { emit(Unit) }

        val result = repository.addFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `deleteFrame should delete frame`() = runBlocking {
        val frame = getFrame()
        every { mockLocalDataSource.deleteFrame(frame) } returns flow { emit(Unit) }

        val result = repository.deleteFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    private fun getFrame(): Frame {
        val player = Player(name = "Rui Chapéu")
        val match = Match(player1 = player, player2 = player)
        return Frame(match = match)
    }

}
