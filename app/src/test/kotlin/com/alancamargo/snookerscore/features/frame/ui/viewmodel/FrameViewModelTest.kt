package com.alancamargo.snookerscore.features.frame.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.frame.data.analytics.FrameAnalytics
import com.alancamargo.snookerscore.features.frame.domain.model.Ball
import com.alancamargo.snookerscore.features.frame.domain.model.Foul
import com.alancamargo.snookerscore.features.frame.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.features.frame.domain.usecase.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.features.frame.domain.usecase.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.match.domain.usecase.DeleteMatchUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.UpdatePlayerStatsWithMatchResultUseCase
import com.alancamargo.snookerscore.testtools.getMatchSummary
import com.alancamargo.snookerscore.testtools.getPlayer
import com.alancamargo.snookerscore.testtools.getPlayerStats
import com.alancamargo.snookerscore.testtools.getUiFrameList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class FrameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAnalytics = mockk<FrameAnalytics>(relaxed = true)
    private val mockAddOrUpdateFrameUseCase = mockk<AddOrUpdateFrameUseCase>()
    private val mockBreakCalculator = mockk<BreakCalculator>(relaxed = true)
    private val mockGetPenaltyValueUseCase = mockk<GetPenaltyValueUseCase>()
    private val mockDeleteMatchUseCase = mockk<DeleteMatchUseCase>()
    private val mockGetPlayerStatsUseCase = mockk<GetPlayerStatsUseCase>()
    private val mockAddOrUpdatePlayerStatsUseCase = mockk<AddOrUpdatePlayerStatsUseCase>()
    private val mockGetMatchSummaryUseCase = mockk<GetMatchSummaryUseCase>()
    private val mockUpdatePlayerStatsWithMatchResultUseCase = mockk<UpdatePlayerStatsWithMatchResultUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<FrameUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<FrameUiAction>>(relaxed = true)

    private lateinit var viewModel: FrameViewModel

    @Test
    fun `at startup should send ShowStartingPlayerPrompt`() {
        createViewModel()

        verify { mockActionObserver.onChanged(any<FrameUiAction.ShowStartingPlayerPrompt>()) }
    }

    @Test
    fun `onStartingPlayerSelected should send ShowFullScreenAds action`() {
        createViewModel()

        viewModel.onStartingPlayerSelected(getPlayer().toUi())

        verify { mockActionObserver.onChanged(FrameUiAction.ShowFullScreenAds) }
    }

    @Test
    fun `onStartingPlayerSelected should track on analytics`() {
        createViewModel()

        viewModel.onStartingPlayerSelected(getPlayer().toUi())

        verify { mockAnalytics.trackFrameStarted() }
    }

    @Test
    fun `onBallPotted should enableUndoLastPottedBall button`() {
        createViewModel()

        viewModel.onBallPotted(Ball.BROWN)

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onBallPotted should register potted ball on calculator`() {
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onBallPotted(Ball.RED)

        verify { mockBreakCalculator.potBall(Ball.RED) }
    }

    @Test
    fun `onBallPotted should update UI state`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onBallPotted(Ball.BLUE)

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onUndoLastPottedBallClicked should track on analytics`() {
        createViewModel()

        viewModel.onUndoLastPottedBallClicked()

        verify { mockAnalytics.trackUndoLastPottedBallClicked() }
    }

    @Test
    fun `onUndoLastPottedBallClicked should update UI state`() {
        createViewModel()

        viewModel.onUndoLastPottedBallClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onObjectBallFoulClicked should send ShowObjectBalls action`() {
        createViewModel()

        viewModel.onObjectBallFoulClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowObjectBalls) }
    }

    @Test
    fun `onFoul should calculate penalty value`() {
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onFoul(Foul.Other)

        verify { mockGetPenaltyValueUseCase.invoke(Foul.Other) }
    }

    @Test
    fun `onUndoLastFoulClicked should track on analytics`() {
        createViewModel()

        viewModel.onUndoLastFoulClicked()

        verify { mockAnalytics.trackUndoLastFoulClicked() }
    }

    @Test
    fun `onUndoLastFoulClicked should update UI state`() {
        createViewModel()

        viewModel.onUndoLastFoulClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should disable undo last potted ball button`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onEndTurnClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should swap current player`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onEndTurnClicked()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndTurnClicked should update frame`() {
        mockSuccessfulResponse()
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndTurnClicked()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndTurnClicked should clear break calculator`() {
        mockSuccessfulResponse()
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndTurnClicked()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `onEndFrameClicked should track on analytics`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onEndFrameClicked()

        verify { mockAnalytics.trackEndFrameClicked() }
    }

    @Test
    fun `onEndFrameClicked should send ShowEndTurnConfirmation`() {
        createViewModel()

        viewModel.onEndFrameClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowEndFrameConfirmation) }
    }

    @Test
    fun `onEndFrameCancelled should track on analytics`() {
        createViewModel()

        viewModel.onEndFrameCancelled()

        verify { mockAnalytics.trackEndFrameCancelled() }
    }

    @Test
    fun `onEndFrameConfirmed should swap current player`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onEndFrameConfirmed()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onEndFrameConfirmed should update frame and swap current`() {
        mockSuccessfulResponse()
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndFrameConfirmed()

        verify { mockAddOrUpdateFrameUseCase.invoke(any()) }
    }

    @Test
    fun `onEndFrameConfirmed should clear break calculator`() {
        mockSuccessfulResponse()
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndFrameConfirmed()

        verify { mockBreakCalculator.clear() }
    }

    @Test
    fun `when not on last frame onEndFrameConfirmed should track on analytics`() {
        mockSuccessfulResponse()
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndFrameConfirmed()

        verify { mockAnalytics.trackEndFrameConfirmed(isEndingMatch = false) }
    }

    @Test
    fun `when not on last frame onEndFrameConfirmed should change current frame`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onEndFrameConfirmed()

        verify { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should track on analytics`() {
        mockSuccessfulResponse()
        createViewModel(isLastFrame = true)
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndFrameConfirmed()

        verify { mockAnalytics.trackEndFrameConfirmed(isEndingMatch = true) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should get match summary`() {
        mockSuccessfulResponse()
        createViewModel(isLastFrame = true)
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndFrameConfirmed()

        verify { mockGetMatchSummaryUseCase.invoke(frames = any()) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should update winning player stats`() {
        mockSuccessfulResponse()
        createViewModel(isLastFrame = true)
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onEndFrameConfirmed()

        verify { mockUpdatePlayerStatsWithMatchResultUseCase.invoke(winnerCurrentStats = any()) }
    }

    @Test
    fun `when on last frame onEndFrameConfirmed should send OpenMatchSummary action`() {
        mockSuccessfulResponse()
        createViewModel(isLastFrame = true)

        viewModel.onEndFrameConfirmed()

        verify { mockActionObserver.onChanged(any<FrameUiAction.OpenMatchSummary>()) }
    }

    @Test
    fun `onForfeitMatchClicked should track on analytics`() {
        createViewModel()

        viewModel.onForfeitMatchClicked()

        verify { mockAnalytics.trackForfeitMatchClicked() }
    }

    @Test
    fun `onForfeitMatchClicked should send ShowForfeitMatchConfirmation`() {
        createViewModel()

        viewModel.onForfeitMatchClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowForfeitMatchConfirmation) }
    }

    @Test
    fun `onForfeitMatchCancelled should track on analytics`() {
        createViewModel()

        viewModel.onForfeitMatchCancelled()

        verify { mockAnalytics.trackForfeitMatchCancelled() }
    }

    @Test
    fun `onForfeitMatchConfirmed should track on analytics`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { emit(Unit) }
        createViewModel()

        viewModel.onForfeitMatchConfirmed()

        verify { mockAnalytics.trackForfeitMatchConfirmed() }
    }

    @Test
    fun `with successful response onForfeitMatchConfirmed should send OpenMain action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { emit(Unit) }
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onForfeitMatchConfirmed()

        verify { mockActionObserver.onChanged(FrameUiAction.OpenMain) }
    }

    @Test
    fun `with error response onForfeitMatchConfirmed should send ShowError action`() {
        every { mockDeleteMatchUseCase.invoke(any()) } returns flow { throw IOException() }
        createViewModel()
        viewModel.onStartingPlayerSelected(player = getPlayer().toUi())

        viewModel.onForfeitMatchConfirmed()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowError) }
    }

    @Test
    fun `onNativeBackClicked should track on analytics`() {
        createViewModel()

        viewModel.onNativeBackClicked()

        verify { mockAnalytics.trackNativeBackClicked() }
    }

    @Test
    fun `onNativeBackClicked should send ShowForfeitMatchConfirmation`() {
        createViewModel()

        viewModel.onNativeBackClicked()

        verify { mockActionObserver.onChanged(FrameUiAction.ShowForfeitMatchConfirmation) }
    }

    private fun configureMocks() {
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
        every { mockGetMatchSummaryUseCase.invoke(frames = any()) } returns getMatchSummary()
        every {
            mockUpdatePlayerStatsWithMatchResultUseCase.invoke(winnerCurrentStats = any())
        } returns flow { emit(Unit) }
    }

    private fun mockSuccessfulResponse() {
        every { mockAddOrUpdateFrameUseCase.invoke(frame = any()) } returns flow { emit(Unit) }
    }

    private fun createViewModel(isLastFrame: Boolean = false) {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        configureMocks()

        viewModel = FrameViewModel(
            frames = getFrames(isLastFrame),
            analytics = mockAnalytics,
            useCases = FrameViewModel.UseCases(
                getMatchSummaryUseCase = mockGetMatchSummaryUseCase,
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

    private fun getFrames(isLastFrame: Boolean): List<UiFrame> {
        val frames = getUiFrameList(player1Id = "12345", player2Id = "54331")

        return if (isLastFrame) {
            frames.apply {
                forEachIndexed { index, frame ->
                    frame.isFinished = index < frames.lastIndex
                }
            }
        } else {
            frames
        }
    }

}
