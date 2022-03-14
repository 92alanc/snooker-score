package com.alancamargo.snookerscore.features.match.ui.viewmodel.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.frame.domain.usecase.GetFramesUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.DeleteMatchUseCase
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.testtools.getFrameList
import com.alancamargo.snookerscore.testtools.getMatchSummary
import com.alancamargo.snookerscore.testtools.getUiMatch
import com.alancamargo.snookerscore.features.frame.ui.mapping.toUi
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MatchDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetFramesUseCase = mockk<GetFramesUseCase>()
    private val mockDeleteMatchUseCase = mockk<DeleteMatchUseCase>()
    private val mockGetMatchSummaryUseCase = mockk<GetMatchSummaryUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<MatchDetailsUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<MatchDetailsUiAction>>(relaxed = true)

    private lateinit var viewModel: MatchDetailsViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = MatchDetailsViewModel(
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
    fun `onDeleteMatchClicked should send ShowDeleteMatchConfirmation`() {
        viewModel.onDeleteMatchClicked()

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.ShowDeleteMatchConfirmation) }
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
