package com.alancamargo.snookerscore.features.match.ui.viewmodel.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.match.data.analytics.list.MatchListAnalytics
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchesUseCase
import com.alancamargo.snookerscore.features.match.ui.mapping.toUi
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getMatchList
import com.alancamargo.snookerscore.testtools.getUiMatch
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MatchListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAnalytics = mockk<MatchListAnalytics>(relaxed = true)
    private val mockGetMatchesUseCase = mockk<GetMatchesUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<MatchListUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<MatchListUiAction>>(relaxed = true)

    private val matches = getMatchList()

    private lateinit var viewModel: MatchListViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = MatchListViewModel(
            analytics = mockAnalytics,
            getMatchesUseCase = mockGetMatchesUseCase,
            logger = mockLogger,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    @After
    fun tearDown() {
        viewModel.action.removeObserver(mockActionObserver)
    }

    @Test
    fun `at startup should track screen viewed on analytics`() {
        verify { mockAnalytics.trackScreenViewed() }
    }

    @Test
    fun `with successful response getMatches should set state with matches`() {
        mockSuccessfulResponse()

        viewModel.getMatches()

        val expected = matches.map { it.toUi() }
        verify { mockStateObserver.onChanged(MatchListUiState(expected)) }
    }

    @Test
    fun `with error response getMatches should send ShowError action`() {
        every { mockGetMatchesUseCase.invoke() } returns flow { throw IOException(ERROR_MESSAGE) }

        viewModel.getMatches()

        verify { mockActionObserver.onChanged(MatchListUiAction.ShowError) }
    }

    @Test
    fun `onNewMatchClicked should track on analytics`() {
        mockSuccessfulResponse()

        viewModel.onNewMatchClicked()

        verify { mockAnalytics.trackNewMatchClicked() }
    }

    @Test
    fun `onNewMatchClicked should send OpenNewMatch action`() {
        mockSuccessfulResponse()

        viewModel.onNewMatchClicked()

        verify { mockActionObserver.onChanged(MatchListUiAction.OpenNewMatch) }
    }

    @Test
    fun `onMatchClicked should track on analytics`() {
        mockSuccessfulResponse()

        val match = getUiMatch()
        viewModel.onMatchClicked(match)

        verify { mockAnalytics.trackMatchCardClicked() }
    }

    @Test
    fun `onMatchClicked should send OpenMatchDetails action`() {
        mockSuccessfulResponse()

        val match = getUiMatch()
        viewModel.onMatchClicked(match)

        verify { mockActionObserver.onChanged(MatchListUiAction.OpenMatchDetails(match)) }
    }

    @Test
    fun `onBackClicked should track on analytics`() {
        viewModel.onBackClicked()

        verify { mockAnalytics.trackBackClicked() }
    }

    @Test
    fun `onBackClicked should send Finish action`() {
        viewModel.onBackClicked()

        verify { mockActionObserver.onChanged(MatchListUiAction.Finish) }
    }

    @Test
    fun `onNativeBackClicked should track on analytics`() {
        viewModel.onNativeBackClicked()

        verify { mockAnalytics.trackNativeBackClicked() }
    }

    @Test
    fun `onNativeBackClicked should send Finish action`() {
        viewModel.onNativeBackClicked()

        verify { mockActionObserver.onChanged(MatchListUiAction.Finish) }
    }

    private fun mockSuccessfulResponse() {
        every { mockGetMatchesUseCase.invoke() } returns flow { emit(matches) }
    }

}
