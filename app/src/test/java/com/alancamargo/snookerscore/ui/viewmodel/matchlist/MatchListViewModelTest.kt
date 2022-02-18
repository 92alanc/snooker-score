package com.alancamargo.snookerscore.ui.viewmodel.matchlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.usecase.match.GetMatchesUseCase
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getMatchList
import com.alancamargo.snookerscore.testtools.getUiMatch
import com.alancamargo.snookerscore.ui.mapping.toUi
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MatchListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetMatchesUseCase = mockk<GetMatchesUseCase>()
    private val mockStateObserver = mockk<Observer<MatchListUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<MatchListUiAction>>(relaxed = true)

    private val matches = getMatchList()

    private lateinit var viewModel: MatchListViewModel

    @After
    fun tearDown() {
        viewModel.action.removeObserver(mockActionObserver)
    }

    @Test
    fun `at startup should send ShowLoading action`() {
        every { mockGetMatchesUseCase.invoke() } returns flow { delay(timeMillis = 50) }

        createViewModel()

        verify { mockActionObserver.onChanged(MatchListUiAction.ShowLoading) }
    }

    @Test
    fun `with successful response at startup should set state with matches`() {
        mockSuccessfulResponse()

        createViewModel()

        val expected = matches.map { it.toUi() }
        verify { mockStateObserver.onChanged(MatchListUiState(expected)) }
    }

    @Test
    fun `with error response at startup should send ShowError action`() {
        every { mockGetMatchesUseCase.invoke() } returns flow { throw IOException(ERROR_MESSAGE) }

        createViewModel()

        verify { mockActionObserver.onChanged(MatchListUiAction.ShowError) }
    }

    @Test
    fun `at startup should send HideLoading action`() {
        mockSuccessfulResponse()

        createViewModel()

        verify { mockActionObserver.onChanged(MatchListUiAction.HideLoading) }
    }

    @Test
    fun `onNewMatchClicked should send OpenNewMatch action`() {
        mockSuccessfulResponse()
        createViewModel()

        viewModel.onNewMatchClicked()

        verify { mockActionObserver.onChanged(MatchListUiAction.OpenNewMatch) }
    }

    @Test
    fun `onMatchClicked should send OpenMatchDetails action`() {
        mockSuccessfulResponse()
        createViewModel()

        val match = getUiMatch()
        viewModel.onMatchClicked(match)

        verify { mockActionObserver.onChanged(MatchListUiAction.OpenMatchDetails(match)) }
    }

    private fun createViewModel() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = MatchListViewModel(
            getMatchesUseCase = mockGetMatchesUseCase,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    private fun mockSuccessfulResponse() {
        every { mockGetMatchesUseCase.invoke() } returns flow { emit(matches) }
    }

}