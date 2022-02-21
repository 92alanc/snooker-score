package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DrawPlayerUseCase
import com.alancamargo.snookerscore.testtools.getUiFrameList
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.model.UiBall
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FrameViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockDrawPlayerUseCase = mockk<DrawPlayerUseCase>()
    private val mockAddOrUpdateFrameUseCase = mockk<AddOrUpdateFrameUseCase>()
    private val mockBreakCalculator = mockk<BreakCalculator>(relaxed = true)
    private val mockGetPenaltyValueUseCase = mockk<GetPenaltyValueUseCase>()
    private val mockStateObserver = mockk<Observer<FrameUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<FrameUiAction>>(relaxed = true)

    private val frames = getUiFrameList()
    private val match = frames.first().match

    private lateinit var viewModel: FrameViewModel

    @Test
    fun `at startup should set current frame and player and undo last potted ball button should be disabled`() {
        createViewModel()

        val firstFrame = frames.first()
        verify {
            mockStateObserver.onChanged(
                FrameUiState(
                    currentFrame = firstFrame,
                    currentPlayer = firstFrame.match.player1,
                    isUndoLastPottedBallButtonEnabled = false
                )
            )
        }
    }

    @Test
    fun `onBallPotted should enableUndoLastPottedBall button`() {
        createViewModel()

        viewModel.onBallPotted(UiBall.BROWN)

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onBallPotted should register potted ball on calculator`() {
        createViewModel()

        viewModel.onBallPotted(UiBall.RED)

        verify { mockBreakCalculator.potBall(Ball.RED) }
    }

    @Test
    fun `onBallPotted should send ShowLoading action`() {
        createViewModel()
        mockDelay()

        viewModel.onBallPotted(UiBall.BLACK)

        verify { mockActionObserver.onChanged(FrameUiAction.ShowLoading) }
    }

    @Test
    fun `with successful response onBallPotted should set state with updated frame`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onBallPotted(UiBall.BLUE)

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `with error response onBallPotted should send ShowError action`() {
        createViewModel()
        mockErrorResponse()

        viewModel.onBallPotted(UiBall.PINK)

        verify { mockActionObserver.onChanged(FrameUiAction.ShowError) }
    }

    @Test
    fun `onBallPotted should send HideLoading action`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onBallPotted(UiBall.GREEN)

        verify { mockActionObserver.onChanged(FrameUiAction.HideLoading) }
    }

    @Test
    fun `onUndoLastPottedBallClicked should undo last potted ball on calculator`() {
        createViewModel()

        viewModel.onUndoLastPottedBallClicked()

        verify { mockBreakCalculator.undoLastPottedBall() }
    }

    @Test
    fun `onUndoLastPottedBallClicked should update frame`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onUndoLastPottedBallClicked()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onFoul should calculate penalty value`() {
        createViewModel()

        viewModel.onFoul(Foul.HitNothing)

        verify { mockGetPenaltyValueUseCase.invoke(Foul.HitNothing) }
    }

    @Test
    fun `onFoul should add or update frame`() {
        createViewModel()
        mockSuccessfulResponse()

        val foul = Foul.BallPotted(Ball.CUE_BALL)
        viewModel.onFoul(foul)

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndTurnClicked should disable undo last potted ball button`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should get break points`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockBreakCalculator.getPoints() }
    }

    @Test
    fun `onEndTurnClicked should swap current player`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should update frame`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndTurnClicked should clear break calculator`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `onEndFrameClicked should get break points`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockBreakCalculator.getPoints() }
    }

    @Test
    fun `onEndFrameClicked should swap current player`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndFrameClicked should update frame and swap current`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndFrameClicked should clear break calculator`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `when not on last frame onEndFrameClicked should change current frame`() {
        createViewModel()
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `when on last frame onEndFrameClicked should send OpenMatchSummary action`() {
        createViewModel()
        mockSuccessfulResponse()

        repeat(3) {
            viewModel.onEndFrameClicked()
        }

        verify { mockActionObserver.onChanged(FrameUiAction.OpenMatchSummary(match)) }
    }

    private fun createViewModel() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        val domainMatch = match.toDomain()
        every {
            mockDrawPlayerUseCase.invoke(domainMatch.player1, domainMatch.player2)
        } returns domainMatch.player1
        every { mockBreakCalculator.getPoints() } returns 10
        every { mockGetPenaltyValueUseCase.invoke(any()) } returns 4

        viewModel = FrameViewModel(
            frames = frames,
            drawPlayerUseCase = mockDrawPlayerUseCase,
            addOrUpdateFrameUseCase = mockAddOrUpdateFrameUseCase,
            breakCalculator = mockBreakCalculator,
            getPenaltyValueUseCase = mockGetPenaltyValueUseCase,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    private fun mockSuccessfulResponse() {
        every { mockAddOrUpdateFrameUseCase.invoke(frame = any()) } returns flow { emit(Unit) }
    }

    private fun mockDelay() {
        every {
            mockAddOrUpdateFrameUseCase.invoke(frame = any())
        } returns flow { delay(timeMillis = 50) }
    }

    private fun mockErrorResponse() {
        every {
            mockAddOrUpdateFrameUseCase.invoke(frame = any())
        } returns flow { throw IOException() }
    }

}
