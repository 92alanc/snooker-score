package com.alancamargo.snookerscore.data.repository

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.local.match.MatchLocalDataSource
import com.alancamargo.snookerscore.testtools.getMatch
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
    fun `addOrUpdateMatch should add or update match`() = runBlocking {
        val match = getMatch()
        every { mockLocalDataSource.addOrUpdateMatch(match) } returns flow { emit(Unit) }

        val result = repository.addOrUpdateMatch(match)

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

}