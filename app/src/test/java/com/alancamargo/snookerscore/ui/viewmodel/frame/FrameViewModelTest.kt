package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DrawPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.testtools.getPlayerStats
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
import org.junit.Before
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
    private val mockDeleteMatchUseCase = mockk<DeleteMatchUseCase>()
    private val mockGetPlayerStatsUseCase = mockk<GetPlayerStatsUseCase>()
    private val mockAddOrUpdatePlayerStatsUseCase = mockk<AddOrUpdatePlayerStatsUseCase>()
    private val mockStateObserver = mockk<Observer<FrameUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<FrameUiAction>>(relaxed = true)

    private val frames = getUiFrameList()
    private val match = frames.first().match

    private lateinit var viewModel: FrameViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        configureMocks()

        viewModel = FrameViewModel(
            frames = frames,
            useCases = FrameViewModel.UseCases(
                drawPlayerUseCase = mockDrawPlayerUseCase,
                addOrUpdateFrameUseCase = mockAddOrUpdateFrameUseCase,
                getPenaltyValueUseCase = mockGetPenaltyValueUseCase,
                deleteMatchUseCase = mockDeleteMatchUseCase,
                getPlayerStatsUseCase = mockGetPlayerStatsUseCase,
                addOrUpdatePlayerStatsUseCase = mockAddOrUpdatePlayerStatsUseCase
            ),
            breakCalculator = mockBreakCalculator,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `at startup should set current frame and player and undo last potted ball button should be disabled`() {
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
        viewModel.onBallPotted(UiBall.BROWN)

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onBallPotted should register potted ball on calculator`() {
        viewModel.onBallPotted(UiBall.RED)

        verify { mockBreakCalculator.potBall(Ball.RED) }
    }

    @Test
    fun `onBallPotted should update UI state`() {
        mockSuccessfulResponse()

        viewModel.onBallPotted(UiBall.BLUE)

        verify(exactly = 4) { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onUndoLastPottedBallClicked should undo last potted ball on calculator`() {
        viewModel.onUndoLastPottedBallClicked()

        verify { mockBreakCalculator.undoLastPottedBall() }
    }

    @Test
    fun `onUndoLastPottedBallClicked should update UI state`() {
        viewModel.onUndoLastPottedBallClicked()

        verify(exactly = 3) { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onFoul should calculate penalty value`() {
        viewModel.onFoul(Foul.HitNothing)

        verify { mockGetPenaltyValueUseCase.invoke(Foul.HitNothing) }
    }

    @Test
    fun `onFoul should add or update frame`() {
        mockSuccessfulResponse()

        val foul = Foul.BallPotted(Ball.CUE_BALL)
        viewModel.onFoul(foul)

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndTurnClicked should disable undo last potted ball button`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should swap current player`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should update frame`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndTurnClicked should clear break calculator`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnClicked()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `onEndFrameClicked should swap current player`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndFrameClicked should update frame and swap current`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndFrameClicked should clear break calculator`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `when not on last frame onEndFrameClicked should change current frame`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `when on last frame onEndFrameClicked should send OpenMatchSummary action`() {
        mockSuccessfulResponse()

        repeat(3) {
            viewModel.onEndFrameClicked()
        }

        verify { mockActionObserver.onChanged(FrameUiAction.OpenMatchSummary(match)) }
    }

    @Test
    fun `onForfeitMatchClicked should send ShowLoading action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { delay(timeMillis = 50) }

        viewModel.onForfeitMatchClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowLoading) }
    }

    @Test
    fun `with successful response onForfeitMatchClicked should send OpenMain action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { emit(Unit) }

        viewModel.onForfeitMatchClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.OpenMain) }
    }

    @Test
    fun `with error response onForfeitMatchClicked should send ShowError action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { throw IOException() }

        viewModel.onForfeitMatchClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowError) }
    }

    @Test
    fun `onForfeitMatchClicked should send HideLoading action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { emit(Unit) }

        viewModel.onForfeitMatchClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.HideLoading) }
    }

    private fun configureMocks() {
        val domainMatch = match.toDomain()
        every {
            mockDrawPlayerUseCase.invoke(domainMatch.player1, domainMatch.player2)
        } returns domainMatch.player1
        every { mockBreakCalculator.getPoints() } returns 10
        every { mockGetPenaltyValueUseCase.invoke(any()) } returns 4
        every {
            mockGetPlayerStatsUseCase.invoke(player = any())
        } returns flow { emit(getPlayerStats()) }
        every {
            mockAddOrUpdatePlayerStatsUseCase.invoke(
                currentPlayerStats = any(),
                frame = any(),
                player = any()
            )
        } returns flow { emit(Unit) }
    }

    private fun mockSuccessfulResponse() {
        every { mockAddOrUpdateFrameUseCase.invoke(frame = any()) } returns flow { emit(Unit) }
    }

}
