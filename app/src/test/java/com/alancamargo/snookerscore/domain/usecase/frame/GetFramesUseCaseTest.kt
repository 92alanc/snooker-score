package com.alancamargo.snookerscore.domain.usecase.frame

import app.cash.turbine.test
import com.alancamargo.snookerscore.domain.repository.FrameRepository
import com.alancamargo.snookerscore.testtools.getFrameList
import com.alancamargo.snookerscore.testtools.getMatch
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class GetFramesUseCaseTest {

    private val mockRepository = mockk<FrameRepository>()
    private val useCase = GetFramesUseCase(mockRepository)

    @Test
    fun `invoke should return frames`() = runBlocking {
        val match = getMatch()
        val expected = getFrameList()
        every { mockRepository.getFrames(match) } returns flow { emit(expected) }

        val result = useCase.invoke(match)

        result.test {
            val item = awaitItem()
            assertThat(item).isEqualTo(expected)
            awaitComplete()
        }
    }

}
