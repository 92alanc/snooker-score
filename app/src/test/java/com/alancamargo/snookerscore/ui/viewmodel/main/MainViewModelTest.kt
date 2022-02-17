package com.alancamargo.snookerscore.ui.viewmodel.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockActionObserver = mockk<Observer<MainUiAction>>(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel().apply {
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `onClickMatches should send OpenMatches action`() {
        viewModel.onClickMatches()

        verify { mockActionObserver.onChanged(MainUiAction.OpenMatches) }
    }

    @Test
    fun `onClickPlayers should send OpenPlayers action`() {
        viewModel.onClickPlayers()

        verify { mockActionObserver.onChanged(MainUiAction.OpenPlayers) }
    }

    @Test
    fun `onClickRules should send OpenRules action`() {
        viewModel.onClickRules()

        verify { mockActionObserver.onChanged(MainUiAction.OpenRules) }
    }

    @Test
    fun `onClickAbout should send ShowAppInfo action`() {
        viewModel.onClickAbout()

        verify { mockActionObserver.onChanged(MainUiAction.ShowAppInfo) }
    }

}
