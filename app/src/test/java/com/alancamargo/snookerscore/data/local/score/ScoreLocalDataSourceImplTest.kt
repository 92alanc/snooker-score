package com.alancamargo.snookerscore.data.local.score

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.ScoreDao
import com.alancamargo.snookerscore.data.mapping.toData
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getFrame
import com.alancamargo.snookerscore.testtools.getScore
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ScoreLocalDataSourceImplTest {

    private val mockDatabase = mockk<ScoreDao>(relaxed = true)
    private val localDataSource = ScoreLocalDataSourceImpl(mockDatabase)

    @Test
    fun `addOrUpdateScore should add or update score to database`() = runBlocking {
        val score = getScore()

        val result = localDataSource.addOrUpdateScore(score)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addOrUpdateScore(score.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdateScore should return error`() = runBlocking {
        val score = getScore()
        coEvery { mockDatabase.addOrUpdateScore(score.toData()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addOrUpdateScore(score)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `getScore should get score from database`() = runBlocking {
        val expected = getScore()
        coEvery { mockDatabase.getScore(expected.frame.id) } returns expected.toData()

        val result = localDataSource.getScore(expected.frame)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

    @Test
    fun `when database throws exception getScore should return error`() = runBlocking {
        val frame = getFrame()
        coEvery { mockDatabase.getScore(frame.id) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.getScore(frame)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
