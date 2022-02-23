package com.alancamargo.snookerscore.domain.usecase.playerstats

import com.alancamargo.snookerscore.domain.repository.PlayerStatsRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddOrUpdatePlayerStatsUseCaseTest {

    private val mockRepository = mockk<PlayerStatsRepository>()
    private val useCase = AddOrUpdatePlayerStatsUseCase(mockRepository)

    @Test
    fun `invoke should add or update player stats`() = runBlocking {
        // TODO: rewrite test
    }

}