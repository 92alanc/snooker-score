package com.alancamargo.snookerscore.features.frame.domain.usecase

import app.cash.turbine.test
import com.alancamargo.snookerscore.features.frame.domain.repository.FrameRepository
import com.alancamargo.snookerscore.testtools.getFrame
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddOrUpdateFrameUseCaseTest {

    private val mockRepository = mockk<FrameRepository>()
    private val useCase = AddOrUpdateFrameUseCase(mockRepository)

    @Test
    fun `invoke should add or update frame`() = runBlocking {
        val frame = getFrame()
        every { mockRepository.addOrUpdateFrame(frame) } returns flow { emit(Unit) }

        val result = useCase.invoke(frame)

        result.test {
            awaitItem()
            awaitComplete()
        }
    }

}
