package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.frame.FrameLocalDataSource
import com.alancamargo.snookerscore.testtools.getFrame
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

}
