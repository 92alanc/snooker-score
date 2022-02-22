package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.usecase.match.AddMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.alancamargo.snookerscore.testtools.ERROR_MESSAGE
import com.alancamargo.snookerscore.testtools.getPlayerList
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiPlayer
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
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class NewMatchViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetPlayersUseCase = mockk<GetPlayersUseCase>()
    private val mockArePlayersTheSameUseCase = mockk<ArePlayersTheSameUseCase>()
    private val mockAddMatchUseCase = mockk<AddMatchUseCase>()
    private val mockStateObserver = mockk<Observer<NewMatchUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<NewMatchUiAction>>(relaxed = true)

    private val players = getPlayerList()

    private lateinit var viewModel: NewMatchViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = NewMatchViewModel(
            getPlayersUseCase = mockGetPlayersUseCase,
            arePlayersTheSameUseCase = mockArePlayersTheSameUseCase,
            addMatchUseCase = mockAddMatchUseCase,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `when loading players getPlayers should send ShowLoading action`() {
        every { mockGetPlayersUseCase.invoke() } returns flow { delay(timeMillis = 50) }

        viewModel.getPlayers()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowLoading) }
    }

    @Test
    fun `with successful response getPlayers should set state with players`() {
        mockSuccessfulPlayersResponse()

        viewModel.getPlayers()

        val expected = players.map { it.toUi() }
        verify { mockStateObserver.onChanged(NewMatchUiState(expected)) }
    }

    @Test
    fun `with error response getPlayers should send ShowError action`() {
        every { mockGetPlayersUseCase.invoke() } returns flow { throw IOException(ERROR_MESSAGE) }

        viewModel.getPlayers()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowError) }
    }

    @Test
    fun `after loading players getPlayers should send HideLoading action`() {
        mockSuccessfulPlayersResponse()

        viewModel.getPlayers()

        verify { mockActionObserver.onChanged(NewMatchUiAction.HideLoading) }
    }

    @Test
    fun `onFieldErased should disable start match button`() {
        mockSuccessfulPlayersResponse()
        viewModel.getPlayers()

        viewModel.onFieldErased()

        verify {
            mockStateObserver.onChanged(
                NewMatchUiState(
                    players = players.map { it.toUi() },
                    isStartMatchButtonEnabled = false
                )
            )
        }
    }

    @Test
    fun `onAllFieldsFilled should enable start match button`() {
        mockSuccessfulPlayersResponse()
        viewModel.getPlayers()

        viewModel.onAllFieldsFilled()

        verify {
            mockStateObserver.onChanged(
                NewMatchUiState(
                    players = players.map { it.toUi() },
                    isStartMatchButtonEnabled = true
                )
            )
        }
    }

    @Test
    fun `when players are the same onStartMatchButtonClicked should send ShowSamePlayersDialogue action`() {
        mockSuccessfulPlayersResponse()
        every { mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any()) } returns true

        val player = UiPlayer(name = "Judd Trump")
        viewModel.onStartMatchButtonClicked(player, player, numberOfFrames = 3)

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowSamePlayersDialogue) }
    }

    @Test
    fun `when creating match onStartMatchButtonClicked should send ShowLoading action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { delay(timeMillis = 50) }

        val player = UiPlayer(name = "Judd Trump")
        viewModel.onStartMatchButtonClicked(player, player, numberOfFrames = 3)

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowLoading) }
    }

    @Test
    fun `when match is successfully added onStartMatchButtonClicked should send StartMatch action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        val player = UiPlayer(name = "Judd Trump")
        viewModel.onStartMatchButtonClicked(player, player, numberOfFrames = 3)

        verify { mockActionObserver.onChanged(any<NewMatchUiAction.StartMatch>()) }
    }

    @Test
    fun `with error response onStartMatchButtonClicked should send ShowError action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { throw IOException() }

        val player = UiPlayer(name = "Judd Trump")
        viewModel.onStartMatchButtonClicked(player, player, numberOfFrames = 3)

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowError) }
    }

    @Test
    fun `after creating match onStartMatchButtonClicked should send HideLoading action`() {
        mockSuccessfulPlayersResponse()
        every {
            mockArePlayersTheSameUseCase.invoke(player1 = any(), player2 = any())
        } returns false
        every { mockAddMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        val player = UiPlayer(name = "Judd Trump")
        viewModel.onStartMatchButtonClicked(player, player, numberOfFrames = 3)

        verify { mockActionObserver.onChanged(NewMatchUiAction.HideLoading) }
    }

    @Test
    fun `onHelpButtonClicked should send ShowHelp action`() {
        mockSuccessfulPlayersResponse()

        viewModel.onHelpButtonClicked()

        verify { mockActionObserver.onChanged(NewMatchUiAction.ShowHelp) }
    }

    private fun mockSuccessfulPlayersResponse() {
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
    }

}
