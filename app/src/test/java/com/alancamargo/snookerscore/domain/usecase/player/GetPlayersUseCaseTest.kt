package com.alancamargo.snookerscore.domain.usecase.player

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.alancamargo.snookerscore.testtools.getPlayerList
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetPlayersUseCaseTest {

    private val mockRepository = mockk<PlayerRepository>()
    private val useCase = GetPlayersUseCase(mockRepository)

    @Test
    fun `invoke should return players`() = runBlocking {
        val expected = getPlayerList()
        every { mockRepository.getPlayers() } returns flow { emit(expected) }

        val result = useCase.invoke()

        result.test {
            val players = awaitItem()
            assertThat(players).isEqualTo(expected)
            awaitComplete()
        }
    }

}
