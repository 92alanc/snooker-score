package com.alancamargo.snookerscore.features.match.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.match.data.local.MatchLocalDataSource
import com.alancamargo.snookerscore.testtools.getMatch
import com.alancamargo.snookerscore.testtools.getMatchList
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MatchRepositoryImplTest {

    private val mockLocalDataSource = mockk<MatchLocalDataSource>()
    private val repository = MatchRepositoryImpl(mockLocalDataSource)

    @Test
    fun `addMatch should add match`() = runBlocking {
        val match = getMatch()
        every { mockLocalDataSource.addMatch(match) } returns flow { emit(Unit) }

        val result = repository.addMatch(match)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `deleteMatch should delete match`() = runBlocking {
        val match = getMatch()
        every { mockLocalDataSource.deleteMatch(match) } returns flow { emit(Unit) }

        val result = repository.deleteMatch(match)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `getMatches should return matches`() = runBlocking {
        val expected = getMatchList()
        every { mockLocalDataSource.getMatches() } returns flow { emit(expected) }

        val result = repository.getMatches()

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
