package com.alancamargo.snookerscore.features.match.ui.viewmodel.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.frame.domain.usecase.GetFramesUseCase
import com.alancamargo.snookerscore.features.frame.ui.mapping.toUi
import com.alancamargo.snookerscore.features.match.data.analytics.details.MatchDetailsAnalytics
import com.alancamargo.snookerscore.features.match.domain.usecase.DeleteMatchUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.testtools.getFrameList
import com.alancamargo.snookerscore.testtools.getMatchSummary
import com.alancamargo.snookerscore.testtools.getUiMatch
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MatchDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAnalytics = mockk<MatchDetailsAnalytics>(relaxed = true)
    private val mockGetFramesUseCase = mockk<GetFramesUseCase>()
    private val mockDeleteMatchUseCase = mockk<DeleteMatchUseCase>()
    private val mockGetMatchSummaryUseCase = mockk<GetMatchSummaryUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<MatchDetailsUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<MatchDetailsUiAction>>(relaxed = true)

    private lateinit var viewModel: MatchDetailsViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = MatchDetailsViewModel(
            mockAnalytics,
            mockGetFramesUseCase,
            mockDeleteMatchUseCase,
            mockGetMatchSummaryUseCase,
            mockLogger,
            testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `at startup should track screen viewed on analytics`() {
        verify { mockAnalytics.trackScreenViewed() }
    }

    @Test
    fun `getMatchDetails should set state with frames and winner`() {
        val expected = getFrameList()
        val matchSummary = getMatchSummary()
        every { mockGetFramesUseCase.invoke(match = any()) } returns flow { emit(expected) }
        every { mockGetMatchSummaryUseCase.invoke(frames = any()) } returns matchSummary

        viewModel.getMatchDetails(getUiMatch())

        val frames = expected.map { it.toUi() }
        verify { mockStateObserver.onChanged(MatchDetailsUiState(winner = null, frames = frames)) }
        verify {
            mockStateObserver.onChanged(
                MatchDetailsUiState(winner = matchSummary.winner.toUi(), frames = frames)
            )
        }
    }

    @Test
    fun `with error getMatchDetails should send ShowErrorAction`() {
        every { mockGetFramesUseCase.invoke(match = any()) } returns flow { throw IOException() }

        viewModel.getMatchDetails(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.ShowError) }
    }

    @Test
    fun `onViewSummaryClicked should track on analytics`() {
        every { mockGetFramesUseCase.invoke(match = any()) } returns flow { emit(getFrameList()) }
        viewModel.getMatchDetails(getUiMatch())

        viewModel.onViewSummaryClicked()

        verify { mockAnalytics.trackViewSummaryClicked() }
    }

    @Test
    fun `onViewSummaryClicked should send ViewSummary action`() {
        every { mockGetFramesUseCase.invoke(match = any()) } returns flow { emit(getFrameList()) }
        viewModel.getMatchDetails(getUiMatch())

        viewModel.onViewSummaryClicked()

        verify { mockActionObserver.onChanged(any<MatchDetailsUiAction.ViewSummary>()) }
    }

    @Test
    fun `onDeleteMatchClicked should track on analytics`() {
        viewModel.onDeleteMatchClicked()

        verify { mockAnalytics.trackDeleteMatchClicked() }
    }

    @Test
    fun `onDeleteMatchClicked should send ShowDeleteMatchConfirmation`() {
        viewModel.onDeleteMatchClicked()

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.ShowDeleteMatchConfirmation) }
    }

    @Test
    fun `onDeleteMatchCancelled should track on analytics`() {
        viewModel.onDeleteMatchCancelled()

        verify { mockAnalytics.trackDeleteMatchCancelled() }
    }

    @Test
    fun `onDeleteMatchConfirmed should track on analytics`() {
        every { mockDeleteMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        viewModel.onDeleteMatchConfirmed(getUiMatch())

        verify { mockAnalytics.trackDeleteMatchConfirmed() }
    }

    @Test
    fun `onDeleteMatchConfirmed should send Finish action`() {
        every { mockDeleteMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        viewModel.onDeleteMatchConfirmed(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.Finish) }
    }

    @Test
    fun `with error onDeleteMatchConfirmed should send ShowErrorAction`() {
        every { mockDeleteMatchUseCase.invoke(match = any()) } returns flow { throw IOException() }

        viewModel.onDeleteMatchConfirmed(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.ShowError) }
    }

}
