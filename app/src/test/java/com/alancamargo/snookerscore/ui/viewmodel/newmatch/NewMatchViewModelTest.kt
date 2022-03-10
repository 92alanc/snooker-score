package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.usecase.match.AddMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.alancamargo.snookerscore.testtools.getPlayerList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class NewMatchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
    @Ignore("Re-write")
    fun `onSelectPlayer1ButtonClicked should send PickPlayer1 action`() {
        mockSuccessfulPlayersResponse()

        viewModel.onSelectPlayer1ButtonClicked()

        //verify { mockActionObserver.onChanged(NewMatchUiAction.PickPlayer1) }
    }

    @Test
    @Ignore("Re-write")
    fun `onSelectPlayer2ButtonClicked should send PickPlayer2 action`() {
        mockSuccessfulPlayersResponse()

        viewModel.onSelectPlayer2ButtonClicked()

        //verify { mockActionObserver.onChanged(NewMatchUiAction.PickPlayer2) }
    }

    @Test
    @Ignore("Re-write")
    fun `when players are the same onStartMatchButtonClicked should send ShowSamePlayersDialogue action`() {
        mockSuccessfulPlayersResponse()
        every { mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any()) } returns true

        /*val player = UiPlayer(name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onPlayer1Selected(player)
        viewModel.onPlayer2Selected(player)*/

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowSamePlayersDialogue) }
    }

    @Test
    @Ignore("Re-write")
    fun `when creating match onStartMatchButtonClicked should send ShowLoading action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { delay(timeMillis = 50) }

        /*val player = UiPlayer(name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onPlayer1Selected(player)
        viewModel.onPlayer2Selected(player)*/

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowLoading) }
    }

    @Test
    @Ignore("Re-write")
    fun `when match is successfully added onStartMatchButtonClicked should send StartMatch action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        /*val player = UiPlayer(name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onPlayer1Selected(player)
        viewModel.onPlayer2Selected(player)*/

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(any<NewMatchUiAction.StartMatch>()) }
    }

    @Test
    @Ignore("Re-write")
    fun `with error response onStartMatchButtonClicked should send ShowError action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { throw IOException() }

        /*val player = UiPlayer(name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onPlayer1Selected(player)
        viewModel.onPlayer2Selected(player)*/

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowError) }
    }

    @Test
    @Ignore("Re-write")
    fun `after creating match onStartMatchButtonClicked should send HideLoading action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        /*val player = UiPlayer(name = "Judd Trump", gender = UiGender.MALE)
        viewModel.onPlayer1Selected(player)
        viewModel.onPlayer2Selected(player)*/

        viewModel.onStartMatchButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.HideLoading) }
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

    private fun mockSuccessfulPlayersResponse() {
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
    }

}
