package com.alancamargo.snookerscore.features.main.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.features.main.domain.usecase.GetRulesUrlUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetRulesUrlUseCase = mockk<GetRulesUrlUseCase>()
    private val mockActionObserver = mockk<Observer<MainUiAction>>(relaxed = true)

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(mockGetRulesUrlUseCase).apply {
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
        val expectedTitleRes = R.string.rules_web_view_title
        val expectedUrl = "https://thepiratebay.org"
        every { mockGetRulesUrlUseCase.invoke() } returns expectedUrl

        viewModel.onClickRules()

        verify {
            mockActionObserver.onChanged(
                MainUiAction.OpenRules(
                    screenTitleRes = expectedTitleRes,
                    url = expectedUrl
                )
            )
        }
    }

    @Test
    fun `onClickAbout should send ShowAppInfo action`() {
        viewModel.onClickAbout()

        verify { mockActionObserver.onChanged(MainUiAction.ShowAppInfo) }
    }

}
