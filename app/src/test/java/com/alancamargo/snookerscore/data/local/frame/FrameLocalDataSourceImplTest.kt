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
    fun `addOrUpdateFrame should add or update frame to database`() = runBlocking {
        val frame = getFrame()

        val result = localDataSource.addOrUpdateFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addOrUpdateFrame(frame.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdateScore should return error`() = runBlocking {
        val score = getFrame()
        coEvery { mockDatabase.addOrUpdateFrame(score.toData()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addOrUpdateFrame(score)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `getFrames should get frames from database`() = runBlocking {
        val expected = getFrameList()
        val match = expected.first().match
        coEvery { mockDatabase.getFrames(match.dateTime) } returns expected.map { it.toData() }

        val result = localDataSource.getFrames(match)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `when database throws exception getFrames should return error`() = runBlocking {
        coEvery { mockDatabase.getFrames(matchDateTime = any()) } throws IOException(ERROR_MESSAGE)

        val match = getMatch()
        val result = localDataSource.getFrames(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
