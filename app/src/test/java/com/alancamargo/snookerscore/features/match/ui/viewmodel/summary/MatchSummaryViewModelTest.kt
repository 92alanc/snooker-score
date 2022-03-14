package com.alancamargo.snookerscore.features.match.ui.viewmodel.summary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.features.match.ui.mapping.toUi
import com.alancamargo.snookerscore.testtools.getMatchSummary
import com.alancamargo.snookerscore.testtools.getUiFrameList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchSummaryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetMatchSummaryUseCase = mockk<GetMatchSummaryUseCase>()
    private val mockStateObserver = mockk<Observer<MatchSummaryUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<MatchSummaryUiAction>>(relaxed = true)

    private lateinit var viewModel: MatchSummaryViewModel

    @Before
    fun setUp() {
        val frames = getUiFrameList()
        every { mockGetMatchSummaryUseCase.invoke(frames = any()) } returns getMatchSummary()

        viewModel = MatchSummaryViewModel(frames, mockGetMatchSummaryUseCase).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `at startup should set state with match summary`() {
        val expected = getMatchSummary().toUi()
        verify { mockStateObserver.onChanged(MatchSummaryUiState(expected)) }
    }

    @Test
    fun `onCloseButtonClicked should send OpenMain action`() {
        viewModel.onCloseButtonClicked()

        verify { mockActionObserver.onChanged(MatchSummaryUiAction.OpenMain) }
    }

    @Test
    fun `onNewMatchButtonClicked should send NewMatch action`() {
        viewModel.onNewMatchButtonClicked()

        verify { mockActionObserver.onChanged(MatchSummaryUiAction.NewMatch) }
    }

}
