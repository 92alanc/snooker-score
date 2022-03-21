package com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.match.data.analytics.newmatch.NewMatchAnalytics
import com.alancamargo.snookerscore.features.match.domain.usecase.AddMatchUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.GetPlayersUseCase
import com.alancamargo.snookerscore.features.player.ui.model.UiGender
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.testtools.getPlayerList
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
class NewMatchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAnalytics = mockk<NewMatchAnalytics>(relaxed = true)
    private val mockGetPlayersUseCase = mockk<GetPlayersUseCase>()
    private val mockArePlayersTheSameUseCase = mockk<ArePlayersTheSameUseCase>()
    private val mockAddMatchUseCase = mockk<AddMatchUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<NewMatchUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<NewMatchUiAction>>(relaxed = true)

    private val players = getPlayerList()

    private lateinit var viewModel: NewMatchViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = NewMatchViewModel(
            analytics = mockAnalytics,
            arePlayersTheSameUseCase = mockArePlayersTheSameUseCase,
            addMatchUseCase = mockAddMatchUseCase,
            logger = mockLogger,
            dispatcher = testCoroutineDispatcher
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
    fun `onSelectPlayer1ButtonClicked should send PickPlayer action`() {
        mockSuccessfulPlayersResponse()

        viewModel.onSelectPlayer1ButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.PickPlayer) }
    }

    @Test
    fun `onSelectPlayer2ButtonClicked should send PickPlayer action`() {
        mockSuccessfulPlayersResponse()

        viewModel.onSelectPlayer2ButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.PickPlayer) }
    }

    @Test
    fun `when players are the same onStartMatchButtonClicked should track on analytics`() {
        mockSuccessfulPlayersResponse()
        every { mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any()) } returns true

        val player = UiPlayer(id = "12345", name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onSelectPlayer1ButtonClicked()
        viewModel.onPlayerSelected(player)
        viewModel.onSelectPlayer2ButtonClicked()
        viewModel.onPlayerSelected(player.copy(id = "54321"))

        viewModel.onStartMatchButtonClicked()

        verify { mockAnalytics.trackSamePlayers() }
    }

    @Test
    fun `when players are the same onStartMatchButtonClicked should send ShowSamePlayersDialogue action`() {
        mockSuccessfulPlayersResponse()
        every { mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any()) } returns true

        val player = UiPlayer(id = "12345", name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onSelectPlayer1ButtonClicked()
        viewModel.onPlayerSelected(player)
        viewModel.onSelectPlayer2ButtonClicked()
        viewModel.onPlayerSelected(player.copy(id = "54321"))

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowSamePlayersDialogue) }
    }

    @Test
    fun `when match is successfully added onStartMatchButtonClicked should track on analytics`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        val player = UiPlayer(id = "12345", name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onSelectPlayer1ButtonClicked()
        viewModel.onPlayerSelected(player)
        viewModel.onSelectPlayer2ButtonClicked()
        viewModel.onPlayerSelected(player.copy(id = "54321"))

        viewModel.onStartMatchButtonClicked()

        verify { mockAnalytics.trackMatchStarted(numberOfFrames = any()) }
    }

    @Test
    fun `when match is successfully added onStartMatchButtonClicked should send StartMatch action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        val player = UiPlayer(id = "12345", name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onSelectPlayer1ButtonClicked()
        viewModel.onPlayerSelected(player)
        viewModel.onSelectPlayer2ButtonClicked()
        viewModel.onPlayerSelected(player.copy(id = "54321"))

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(any<NewMatchUiAction.StartMatch>()) }
    }

    @Test
    fun `with error response onStartMatchButtonClicked should send ShowError action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { throw IOException() }

        val player = UiPlayer(id = "12345", name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onSelectPlayer1ButtonClicked()
        viewModel.onPlayerSelected(player)
        viewModel.onSelectPlayer2ButtonClicked()
        viewModel.onPlayerSelected(player.copy(id = "54321"))

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowError) }
    }

    @Test
    fun `onNumberOfFramesIncreased should increase number of frames`() {
        viewModel.onNumberOfFramesIncreased()

        verify { mockStateObserver.onChanged(NewMatchUiState(numberOfFrames = 3)) }
    }

    @Test
    fun `when on initial number of frames onDecreaseNumberOfFrames should not decrease number of frames`() {
        viewModel.onNumberOfFramesDecreased()

        verify(exactly = 1) { mockStateObserver.onChanged(any()) }
    }

    @Test
    fun `onDecreaseNumberOfFrames should decrease number of frames by 2`() {
        viewModel.onNumberOfFramesIncreased()

        viewModel.onNumberOfFramesDecreased()

        verify(exactly = 2) { mockStateObserver.onChanged(NewMatchUiState(numberOfFrames = 1)) }
    }

    @Test
    fun `onBackClicked should track on analytics`() {
        viewModel.onBackClicked()

        verify { mockAnalytics.trackBackClicked() }
    }

    @Test
    fun `onBackClicked should send Finish action`() {
        viewModel.onBackClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.Finish) }
    }

    @Test
    fun `onNativeBackClicked should track on analytics`() {
        viewModel.onNativeBackClicked()

        verify { mockAnalytics.trackNativeBackClicked() }
    }

    @Test
    fun `onNativeBackClicked should send Finish action`() {
        viewModel.onNativeBackClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.Finish) }
    }

    private fun mockSuccessfulPlayersResponse() {
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
    }

}
