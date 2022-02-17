package com.alancamargo.snookerscore.data.local.frame

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.FrameDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getFrame
import com.alancamargo.snookerscore.testtools.getFrameList
import com.alancamargo.snookerscore.testtools.getMatch
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class FrameLocalDataSourceImplTest {

    private val mockDatabase = mockk<FrameDao>(relaxed = true)
    private val localDataSource = FrameLocalDataSourceImpl(mockDatabase)

    @Test
    fun `addFrame should add frame to database`() = runBlocking {
        val frame = getFrame()
        val result = localDataSource.addFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addFrame(frame.toData()) }
    }

    @Test
    fun `when database throws exception addFrame should return error`() = runBlocking {
        val frame = getFrame()
        coEvery { mockDatabase.addFrame(frame.toData()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addFrame(frame)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `deleteFrame should delete frame from database`() = runBlocking {
        val frame = getFrame()
        val result = localDataSource.deleteFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.deleteFrame(frame.id) }
    }

    @Test
    fun `when database throws exception deleteFrame should return error`() = runBlocking {
        val frame = getFrame()
        coEvery { mockDatabase.deleteFrame(frame.id) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.deleteFrame(frame)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `getFrames should return frames from database`() = runBlocking {
        val player1Id = "123"
        val player2Id = "456"
        val expected = getFrameList(player1Id, player2Id)
        val match = expected.first().match
        coEvery { mockDatabase.getFrames(match.dateTime) } returns expected.map { it.toData() }

        val result = localDataSource.getFrames(match)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }

        coVerify { mockDatabase.getFrames(match.dateTime) }
    }

    @Test
    fun `when database throws exception getFrames should return error`() = runBlocking {
        val match = getMatch()
        coEvery { mockDatabase.getFrames(match.dateTime) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.getFrames(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
