package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.score.ScoreLocalDataSource
import com.alancamargo.snookerscore.testtools.getFrame
import com.alancamargo.snookerscore.testtools.getScore
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ScoreRepositoryImplTest {

    private val mockLocalDataSource = mockk<ScoreLocalDataSource>()
    private val repository = ScoreRepositoryImpl(mockLocalDataSource)

    @Test
    fun `addOrUpdateScore should add or update score`() = runBlocking {
        val score = getScore()
        every { mockLocalDataSource.addOrUpdateScore(score) } returns flow { emit(Unit) }

        val result = repository.addOrUpdateScore(score)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `getScore should return score`() = runBlocking {
        val frame = getFrame()
        val expected = getScore()
        every { mockLocalDataSource.getScore(frame) } returns flow { emit(expected) }

        val result = repository.getScore(frame)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
