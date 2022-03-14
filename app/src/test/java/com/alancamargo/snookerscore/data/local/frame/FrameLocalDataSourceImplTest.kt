package com.alancamargo.snookerscore.data.local.frame

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.FrameDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.features.match.data.db.MatchDao
import com.alancamargo.snookerscore.features.match.data.mapping.toData
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

    private val mockFrameDao = mockk<FrameDao>(relaxed = true)
    private val mockMatchDao = mockk<MatchDao>(relaxed = true)
    private val localDataSource = FrameLocalDataSourceImpl(mockFrameDao, mockMatchDao)

    @Test
    fun `when frame does not exist addOrUpdateFrame should add frame to database`() = runBlocking {
        val frame = getFrame()
        coEvery { mockFrameDao.getFrame(frame.id) } returns null

        val result = localDataSource.addOrUpdateFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockFrameDao.addFrame(frame.toData()) }
    }

    @Test
    fun `when frame exists addOrUpdateFrame should update frame on database`() = runBlocking {
        val frame = getFrame()
        coEvery { mockFrameDao.getFrame(frame.id) } returns frame.toData()

        val result = localDataSource.addOrUpdateFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockFrameDao.updateFrame(frame.toData()) }
    }

    @Test
    fun `when match has ended addOrUpdateFrame should update match on database`() = runBlocking {
        val frame = getFrame().copy(positionInMatch = 3, isFinished = true)
        coEvery { mockFrameDao.getFrame(frame.id) } returns frame.toData()

        val result = localDataSource.addOrUpdateFrame(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockFrameDao.updateFrame(frame.toData()) }
        val expectedMatch = frame.match.toData().copy(isFinished = true)
        coVerify { mockMatchDao.updateMatch(expectedMatch) }
    }

    @Test
    fun `when database throws exception addOrUpdateFrame should return error`() = runBlocking {
        val frame = getFrame()
        coEvery { mockFrameDao.getFrame(frame.id) } returns null
        coEvery { mockFrameDao.addFrame(frame.toData()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addOrUpdateFrame(frame)

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
        coEvery { mockFrameDao.getFrames(match.dateTime) } returns expected.map { it.toData() }

        val result = localDataSource.getFrames(match)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `when database throws exception getFrames should return error`() = runBlocking {
        coEvery { mockFrameDao.getFrames(matchDateTime = any()) } throws IOException(ERROR_MESSAGE)

        val match = getMatch()
        val result = localDataSource.getFrames(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
