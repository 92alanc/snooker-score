package com.alancamargo.snookerscore.domain.usecase.player

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.PlayerRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class HasPlayersUseCaseTest {

    private val mockRepository = mockk<PlayerRepository>()
    private val useCase = HasPlayersUseCase(mockRepository)

    @Test
    fun `invoke should return whether database has players`() = runBlocking {
        every { mockRepository.hasPlayers() } returns flow { emit(true) }

        val result = useCase.invoke()

        result.test {
            val item = awaitItem()
            assertThat(item).isTrue()
            awaitComplete()
        }
    }

}
