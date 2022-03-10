package com.alancamargo.snookerscore.ui.viewmodel.matchsummary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchSummaryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockActionObserver = mockk<Observer<MatchSummaryUiAction>>(relaxed = true)

    private lateinit var viewModel: MatchSummaryViewModel

    @Before
    fun setUp() {
        viewModel = MatchSummaryViewModel().apply {
            action.observeForever(mockActionObserver)
        }
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
