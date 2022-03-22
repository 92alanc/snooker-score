package com.alancamargo.snookerscore.features.frame.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.frame.data.local.FrameLocalDataSource
import com.alancamargo.snookerscore.testtools.getFrame
import com.alancamargo.snookerscore.testtools.getFrameList
import com.alancamargo.snookerscore.testtools.getMatch
import com.google.common.truth.Truth.assertThat
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
    fun `addOrUpdateFrame should add or update frame`() = runBlocking {
        val score = getFrame()
        every { mockLocalDataSource.addOrUpdateFrame(score) } returns flow { emit(Unit) }

        val result = repository.addOrUpdateFrame(score)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `getFrames should return frames`() = runBlocking {
        val match = getMatch()
        val expected = getFrameList()
        every { mockLocalDataSource.getFrames(match) } returns flow { emit(expected) }

        val result = repository.getFrames(match)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
