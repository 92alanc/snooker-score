package com.alancamargo.snookerscore.data.local.match

import app.cash.turbine.test
import com.alancamargo.snookerscore.data.db.MatchDao
import com.alancamargo.snookerscore.data.mapping.toData
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
class MatchLocalDataSourceImplTest {

    private val mockDatabase = mockk<MatchDao>(relaxed = true)
    private val localDataSource = MatchLocalDataSourceImpl(mockDatabase)

    @Test
    fun `addOrUpdateMatch should add or update match to database`() = runBlocking {
        val match = getMatch()
        val result = localDataSource.addOrUpdateMatch(match)

        result.test {
            awaitItem()
            awaitComplete()
        }

        coVerify { mockDatabase.addOrUpdateMatch(match.toData()) }
    }

    @Test
    fun `when database throws exception addOrUpdateMatch should return error`() = runBlocking {
        val match = getMatch()

        val message = "Fools mock but they shall mourn"
        coEvery { mockDatabase.addOrUpdateMatch(match.toData()) } throws IOException(message)

        val result = localDataSource.addOrUpdateMatch(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
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

        coVerify { mockDatabase.deleteMatch(match.dateTime) }
    }

    @Test
    fun `when database throws exception deleteMatch should return error`() = runBlocking {
        val match = getMatch()

        val message = "Could not delete match"
        coEvery { mockDatabase.deleteMatch(match.dateTime) } throws IOException(message)

        val result = localDataSource.deleteMatch(match)

        result.test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo(message)
        }
    }

}
