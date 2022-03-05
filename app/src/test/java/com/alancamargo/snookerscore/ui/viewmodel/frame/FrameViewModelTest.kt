package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DrawPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetWinningPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.UpdatePlayerStatsWithMatchResultUseCase
import com.alancamargo.snookerscore.testtools.getPlayerStats
import com.alancamargo.snookerscore.testtools.getUiFrameList
import com.alancamargo.snookerscore.ui.mapping.toDomain
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
    private val mockGetWinningPlayerUseCase = mockk<GetWinningPlayerUseCase>()
    private val mockUpdatePlayerStatsWithMatchResultUseCase = mockk<UpdatePlayerStatsWithMatchResultUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
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
                playerUseCases = FrameViewModel.PlayerUseCases(
                    drawPlayerUseCase = mockDrawPlayerUseCase,
                    getWinningPlayerUseCase = mockGetWinningPlayerUseCase
                ),
                playerStatsUseCases = FrameViewModel.PlayerStatsUseCases(
                    getPlayerStatsUseCase = mockGetPlayerStatsUseCase,
                    addOrUpdatePlayerStatsUseCase = mockAddOrUpdatePlayerStatsUseCase,
                    updatePlayerStatsWithMatchResultUseCase = mockUpdatePlayerStatsWithMatchResultUseCase
                ),
                addOrUpdateFrameUseCase = mockAddOrUpdateFrameUseCase,
                getPenaltyValueUseCase = mockGetPenaltyValueUseCase,
                deleteMatchUseCase = mockDeleteMatchUseCase
            ),
            breakCalculator = mockBreakCalculator,
            logger = mockLogger,
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
        viewModel.onBallPotted(Ball.BROWN)

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onBallPotted should register potted ball on calculator`() {
        viewModel.onBallPotted(Ball.RED)

        verify { mockBreakCalculator.potBall(Ball.RED) }
    }

    @Test
    fun `onBallPotted should update UI state`() {
        mockSuccessfulResponse()

        viewModel.onBallPotted(Ball.BLUE)

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

        verify(exactly = 4) { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onFoul should calculate penalty value`() {
        viewModel.onFoul(Foul.Other)

        verify { mockGetPenaltyValueUseCase.invoke(Foul.Other) }
    }

    @Test
    fun `onUndoLastFoulClicked should update UI state`() {
        viewModel.onUndoLastFoulClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should send ShowEndTurnConfirmation`() {
        viewModel.onEndTurnClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowEndTurnConfirmation) }
    }

    @Test
    fun `onEndTurnConfirmed should disable undo last potted ball button`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnConfirmed()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnConfirmed should swap current player`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnConfirmed()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnConfirmed should update frame`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnConfirmed()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndTurnConfirmed should clear break calculator`() {
        mockSuccessfulResponse()

        viewModel.onEndTurnConfirmed()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `onEndFrameClicked should send ShowEndTurnConfirmation`() {
        viewModel.onEndFrameClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowEndFrameConfirmation) }
    }

    @Test
    fun `onEndFrameConfirmed should swap current player`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameConfirmed()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndFrameConfirmed should update frame and swap current`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameConfirmed()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndFrameConfirmed should clear break calculator`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameConfirmed()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `when not on last frame onEndFrameConfirmed should change current frame`() {
        mockSuccessfulResponse()

        viewModel.onEndFrameConfirmed()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should get winning player`() {
        mockSuccessfulResponse()

        repeat(3) {
            viewModel.onEndFrameConfirmed()
        }

        verify { mockGetWinningPlayerUseCase.invoke(frames = any()) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should update winning player stats`() {
        mockSuccessfulResponse()

        repeat(3) {
            viewModel.onEndFrameConfirmed()
        }

        verify { mockUpdatePlayerStatsWithMatchResultUseCase.invoke(winnerCurrentStats = any()) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should send OpenMatchSummary action`() {
        mockSuccessfulResponse()

        repeat(3) {
            viewModel.onEndFrameConfirmed()
        }

        verify { mockActionObserver.onChanged(FrameUiAction.OpenMatchSummary(match)) }
    }

    @Test
    fun `onForfeitMatchClicked should send ShowForfeitMatchConfirmation`() {
        viewModel.onForfeitMatchClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowForfeitMatchConfirmation) }
    }

    @Test
    fun `onForfeitMatchConfirmed should send ShowLoading action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { delay(timeMillis = 50) }

        viewModel.onForfeitMatchConfirmed()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowLoading) }
    }

    @Test
    fun `with successful response onForfeitMatchConfirmed should send OpenMain action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { emit(Unit) }

        viewModel.onForfeitMatchConfirmed()

        verify { mockActionObserver.onChanged(FrameUiAction.OpenMain) }
    }

    @Test
    fun `with error response onForfeitMatchConfirmed should send ShowError action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { throw IOException() }

        viewModel.onForfeitMatchConfirmed()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowError) }
    }

    @Test
    fun `onForfeitMatchConfirmed should send HideLoading action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { emit(Unit) }

        viewModel.onForfeitMatchConfirmed()

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
        every { mockGetWinningPlayerUseCase.invoke(frames = any()) } returns domainMatch.player1
        every {
            mockUpdatePlayerStatsWithMatchResultUseCase.invoke(winnerCurrentStats = any())
        } returns flow { emit(Unit) }
    }

    private fun mockSuccessfulResponse() {
        every { mockAddOrUpdateFrameUseCase.invoke(frame = any()) } returns flow { emit(Unit) }
    }

}
