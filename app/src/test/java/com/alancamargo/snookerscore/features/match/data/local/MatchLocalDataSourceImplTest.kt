package com.alancamargo.snookerscore.features.match.data.local

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.match.data.db.MatchDao
import com.alancamargo.snookerscore.features.match.data.mapping.toData
import com.alancamargo.snookerscore.features.player.data.db.PlayerDao
import com.alancamargo.snookerscore.features.player.data.mapping.toData
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getMatch
import com.alancamargo.snookerscore.testtools.getMatchList
import com.alancamargo.snookerscore.testtools.getPlayer
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MatchLocalDataSourceImplTest {

    private val mockMatchDao = mockk<MatchDao>(relaxed = true)
    private val mockPlayerDao = mockk<PlayerDao>()
    private val localDataSource = MatchLocalDataSourceImpl(mockMatchDao, mockPlayerDao)

    @Test
    fun `addMatch should add match to database`() = runBlocking {
        val match = getMatch()
        val result = localDataSource.addMatch(match)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockMatchDao.addMatch(match.toData()) }
    }

    @Test
    fun `when database throws exception addMatch should return error`() = runBlocking {
        val match = getMatch()
        coEvery { mockMatchDao.addMatch(match.toData()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.addMatch(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `deleteMatch should remove match from database`() = runBlocking {
        val match = getMatch()
        val result = localDataSource.deleteMatch(match)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockMatchDao.deleteMatch(match.toData()) }
    }

    @Test
    fun `when database throws exception deleteMatch should return error`() = runBlocking {
        val match = getMatch()
        coEvery { mockMatchDao.deleteMatch(match.toData()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.deleteMatch(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `getMatches should return matches`() = runBlocking {
        val player1Id = "12345"
        val player2Id = "54321"
        val expected = getMatchList(player1Id, player2Id).map { it.copy(isFinished = true) }

        coEvery { mockMatchDao.getMatches() } returns expected.map { it.toData() }
        coEvery {
            mockPlayerDao.getPlayer(playerId = player1Id)
        } returns expected.first().player1.toData()
        coEvery {
            mockPlayerDao.getPlayer(playerId = player2Id)
        } returns expected.first().player2.toData()

        val result = localDataSource.getMatches()

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }

        coVerify { mockMatchDao.getMatches() }
        coVerify(exactly = expected.size * 2) { mockPlayerDao.getPlayer(playerId = any()) }
    }

    @Test
    fun `getMatches should delete unfinished matches before returning`() = runBlocking {
        val player1Id = "12345"
        val player2Id = "54321"
        val expected = getMatchList(player1Id, player2Id)

        coEvery { mockMatchDao.getMatches() } returns expected.map { it.toData() }
        coEvery {
            mockPlayerDao.getPlayer(playerId = player1Id)
        } returns expected.first().player1.toData()
        coEvery {
            mockPlayerDao.getPlayer(playerId = player2Id)
        } returns expected.first().player2.toData()

        val result = localDataSource.getMatches()

        result.test {
            val item = awaitItem()
            assertThat(item).isEmpty()
            awaitComplete()
        }

        coVerify { mockMatchDao.getMatches() }
        coVerify(exactly = expected.size) { mockMatchDao.deleteMatch(match = any()) }
        coVerify(exactly = expected.size * 2) { mockPlayerDao.getPlayer(playerId = any()) }
    }

    @Test
    fun `when match database throws exception getMatches should return error`() = runBlocking {
        coEvery { mockMatchDao.getMatches() } throws IOException(ERROR_MESSAGE)
        coEvery { mockPlayerDao.getPlayer(any()) } returns getPlayer().toData()

        val result = localDataSource.getMatches()

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

    @Test
    fun `when player database throws exception getMatches should return error`() = runBlocking {
        coEvery { mockMatchDao.getMatches() } returns getMatchList().map { it.toData() }
        coEvery { mockPlayerDao.getPlayer(any()) } throws IOException(ERROR_MESSAGE)

        val result = localDataSource.getMatches()

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(ERROR_MESSAGE)
        }
    }

}
