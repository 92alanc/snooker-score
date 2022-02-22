package com.alancamargo.snookerscore.ui.viewmodel.playerlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.usecase.player.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DeletePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.alancamargo.snookerscore.testtools.getPlayer
import com.alancamargo.snookerscore.testtools.getPlayerList
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
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class PlayerListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAddOrUpdatePlayerUseCase = mockk<AddOrUpdatePlayerUseCase>()
    private val mockDeletePlayerUseCase = mockk<DeletePlayerUseCase>()
    private val mockGetPlayersUseCase = mockk<GetPlayersUseCase>()
    private val mockStateObserver = mockk<Observer<PlayerListUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<PlayerListUiAction>>(relaxed = true)

    private lateinit var viewModel: PlayerListViewModel

    @Test
    fun `at startup should send ShowLoading action`() {
        every { mockGetPlayersUseCase.invoke() } returns flow { delay(timeMillis = 50) }

        createViewModel()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowLoading) }
    }

    @Test
    fun `at startup should set state with players`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }

        createViewModel()

        val expected = players.map { it.toUi() }
        verify { mockStateObserver.onChanged(PlayerListUiState(expected)) }
    }

    @Test
    fun `at startup should send HideLoading action`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }

        createViewModel()

        verify { mockActionObserver.onChanged(PlayerListUiAction.HideLoading) }
    }

    @Test
    fun `with error getting players at startup should send ShowError action`() {
        every { mockGetPlayersUseCase.invoke() } returns flow { throw IOException() }

        createViewModel()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowError) }
    }

    @Test
    fun `onNewPlayerClicked should send ShowNewPlayerDialogue action`() {
        createViewModel()

        viewModel.onNewPlayerClicked()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowNewPlayerDialogue) }
    }

    @Test
    fun `onPlayerSelected should send SelectPlayer action`() {
        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onPlayerSelected(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.SelectPlayer(player)) }
    }

    @Test
    fun `onSavePlayerClicked should send ShowLoading action`() {
        every { mockAddOrUpdatePlayerUseCase.invoke(any()) } returns flow { delay(timeMillis = 50) }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onSavePlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowLoading) }
    }

    @Test
    fun `onSavePlayerClicked should refresh players`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        every { mockAddOrUpdatePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onSavePlayerClicked(player)

        verify { mockGetPlayersUseCase.invoke() }
    }

    @Test
    fun `onSavePlayerClicked should send HideLoading action`() {
        every { mockAddOrUpdatePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onSavePlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.HideLoading) }
    }

    @Test
    fun `with error onSavePlayerClicked should send ShowError action`() {
        every { mockAddOrUpdatePlayerUseCase.invoke(any()) } returns flow { throw IOException() }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onSavePlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowError) }
    }

    @Test
    fun `onDeletePlayerClicked should send ShowLoading action`() {
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { delay(timeMillis = 50) }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowLoading) }
    }

    @Test
    fun `onDeletePlayerClicked should refresh players`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerClicked(player)

        verify { mockGetPlayersUseCase.invoke() }
    }

    @Test
    fun `onDeletePlayerClicked should send HideLoading action`() {
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.HideLoading) }
    }

    @Test
    fun `with error onDeletePlayerClicked should send ShowError action`() {
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { throw IOException() }

        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowError) }
    }

    private fun createViewModel() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = PlayerListViewModel(
            addOrUpdatePlayerUseCase = mockAddOrUpdatePlayerUseCase,
            deletePlayerUseCase = mockDeletePlayerUseCase,
            getPlayersUseCase = mockGetPlayersUseCase,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

}
