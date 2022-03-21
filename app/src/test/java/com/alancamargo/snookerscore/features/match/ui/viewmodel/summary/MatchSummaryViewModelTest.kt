package com.alancamargo.snookerscore.features.match.ui.viewmodel.summary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.features.match.data.analytics.summary.MatchSummaryAnalytics
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

    private val mockAnalytics = mockk<MatchSummaryAnalytics>(relaxed = true)
    private val mockGetMatchSummaryUseCase = mockk<GetMatchSummaryUseCase>()
    private val mockStateObserver = mockk<Observer<MatchSummaryUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<MatchSummaryUiAction>>(relaxed = true)

    private lateinit var viewModel: MatchSummaryViewModel

    @Before
    fun setUp() {
        val frames = getUiFrameList()
        every { mockGetMatchSummaryUseCase.invoke(frames = any()) } returns getMatchSummary()

        viewModel = MatchSummaryViewModel(
            frames,
            mockGetMatchSummaryUseCase,
            mockAnalytics
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
    fun `at startup should set state with match summary`() {
        val expected = getMatchSummary().toUi()
        verify { mockStateObserver.onChanged(MatchSummaryUiState(expected)) }
    }

    @Test
    fun `onNewMatchButtonClicked should track on analytics`() {
        viewModel.onNewMatchButtonClicked()

        verify { mockAnalytics.trackNewMatchClicked() }
    }

    @Test
    fun `onNewMatchButtonClicked should send NewMatch action`() {
        viewModel.onNewMatchButtonClicked()

        verify { mockActionObserver.onChanged(MatchSummaryUiAction.NewMatch) }
    }

    @Test
    fun `onBackClicked should track on analytics`() {
        viewModel.onBackClicked()

        verify { mockAnalytics.trackBackClicked() }
    }

    @Test
    fun `onBackClicked should send OpenMain action`() {
        viewModel.onBackClicked()

        verify { mockActionObserver.onChanged(MatchSummaryUiAction.OpenMain) }
    }

    @Test
    fun `onNativeBackClicked should track on analytics`() {
        viewModel.onNativeBackClicked()

        verify { mockAnalytics.trackNativeBackClicked() }
    }

    @Test
    fun `onNativeBackClicked should send OpenMain action`() {
        viewModel.onNativeBackClicked()

        verify { mockActionObserver.onChanged(MatchSummaryUiAction.OpenMain) }
    }

}
