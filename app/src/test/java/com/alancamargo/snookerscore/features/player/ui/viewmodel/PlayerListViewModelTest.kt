package com.alancamargo.snookerscore.features.player.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.core.data.preferences.PreferenceManager
import com.alancamargo.snookerscore.features.player.data.analytics.PlayerListAnalytics
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.domain.usecase.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.GetPlayersUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.testtools.getPlayer
import com.alancamargo.snookerscore.testtools.getPlayerList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class PlayerListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAddOrUpdatePlayerUseCase = mockk<AddOrUpdatePlayerUseCase>()
    private val mockAddOrUpdatePlayerStatsUseCase = mockk<AddOrUpdatePlayerStatsUseCase>()
    private val mockGetPlayersUseCase = mockk<GetPlayersUseCase>()
    private val mockAnalytics = mockk<PlayerListAnalytics>(relaxed = true)
    private val mockPreferenceManager = mockk<PreferenceManager>(relaxed = true)
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<PlayerListUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<PlayerListUiAction>>(relaxed = true)

    private lateinit var viewModel: PlayerListViewModel

    @Test
    fun `at startup should track screen viewed on analytics`() {
        createViewModel()

        verify { mockAnalytics.trackScreenViewed() }
    }

    @Test
    fun `getPlayers should set state with players`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        createViewModel()

        viewModel.getPlayers()

        val expected = players.map { it.toUi() }
        verify { mockStateObserver.onChanged(PlayerListUiState(expected)) }
    }

    @Test
    fun `when player list is not empty getPlayers should send ShowTip action`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        createViewModel(shouldShowTip = true)

        viewModel.getPlayers()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowTip) }
    }

    @Test
    fun `when player list is empty getPlayers should not send ShowTip action`() {
        val players = emptyList<Player>()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        createViewModel(shouldShowTip = true)

        viewModel.getPlayers()

        verify(exactly = 0) { mockActionObserver.onChanged(PlayerListUiAction.ShowTip) }
    }

    @Test
    fun `when preference manager returns false getPlayers should not send ShowTip action`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        createViewModel()

        viewModel.getPlayers()

        verify(exactly = 0) { mockActionObserver.onChanged(PlayerListUiAction.ShowTip) }
    }

    @Test
    fun `with error getting players getPlayers should send ShowError action`() {
        every { mockGetPlayersUseCase.invoke() } returns flow { throw IOException() }
        createViewModel()

        viewModel.getPlayers()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowError) }
    }

    @Test
    fun `onDontShowTipAgainClicked should track on analytics`() {
        createViewModel(shouldShowTip = true)

        viewModel.onDontShowTipAgainClicked()

        verify { mockAnalytics.trackDoNotShowTipAgainClicked() }
    }

    @Test
    fun `onDontShowTipAgainClicked should update preference on preference manager`() {
        createViewModel(shouldShowTip = true)

        viewModel.onDontShowTipAgainClicked()

        verify { mockPreferenceManager.dontShowPlayerListTipAgain() }
    }

    @Test
    fun `onTipDismissed should track on analytics`() {
        createViewModel(shouldShowTip = true)

        viewModel.onTipDismissed()

        verify { mockAnalytics.trackTipDismissed() }
    }

    @Test
    fun `onNewPlayerClicked should track on analytics`() {
        createViewModel()

        viewModel.onNewPlayerClicked()

        verify { mockAnalytics.trackNewPlayerClicked() }
    }

    @Test
    fun `onNewPlayerClicked should send ShowNewPlayerDialogue action`() {
        createViewModel()

        viewModel.onNewPlayerClicked()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowNewPlayerDialogue) }
    }

    @Test
    fun `onPlayerClicked should track on analytics`() {
        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onPlayerClicked(player)

        verify { mockAnalytics.trackPlayerCardClicked() }
    }

    @Test
    fun `onPlayerClicked should send OpenPlayerStats action`() {
        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onPlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.OpenPlayerStats(player)) }
    }

    @Test
    fun `when picking player onPlayerClicked should send PickPlayer action`() {
        createViewModel(isPickingPlayer = true)

        val player = getPlayer().toUi()
        viewModel.onPlayerClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.PickPlayer(player)) }
    }

    @Test
    fun `onSavePlayerClicked should track on analytics`() {
        createViewModel()

        viewModel.onSavePlayerClicked()

        verify { mockAnalytics.trackSavePlayerClicked() }
    }

    @Test
    fun `onSavePlayerClicked should refresh players`() {
        val players = getPlayerList()
        every { mockGetPlayersUseCase.invoke() } returns flow { emit(players) }
        every { mockAddOrUpdatePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }
        every { mockAddOrUpdatePlayerStatsUseCase.invoke(any()) } returns flow { emit(Unit) }

        createViewModel()

        val player = getPlayer().toUi()
        with(viewModel) {
            setPlayerName(player.name)
            setPlayerGenderOrdinal(player.gender.ordinal)
        }
        viewModel.onSavePlayerClicked()

        verify { mockGetPlayersUseCase.invoke() }
    }

    @Test
    fun `with error onSavePlayerClicked should send ShowError action`() {
        every { mockAddOrUpdatePlayerUseCase.invoke(any()) } returns flow { throw IOException() }
        every { mockAddOrUpdatePlayerStatsUseCase.invoke(any()) } returns flow { emit(Unit) }

        createViewModel()

        val player = getPlayer().toUi()
        with(viewModel) {
            setPlayerName(player.name)
            setPlayerGenderOrdinal(player.gender.ordinal)
        }
        viewModel.onSavePlayerClicked()

        verify { mockActionObserver.onChanged(PlayerListUiAction.ShowError) }
    }

    @Test
    fun `onPlayerLongClicked should track on analytics`() {
        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onPlayerLongClicked(player)

        verify { mockAnalytics.trackPlayerCardLongClicked() }
    }

    @Test
    fun `onPlayerLongClicked should send EditPlayer action`() {
        createViewModel()

        val player = getPlayer().toUi()
        viewModel.onPlayerLongClicked(player)

        verify { mockActionObserver.onChanged(PlayerListUiAction.EditPlayer(player)) }
    }

    @Test
    fun `onBackClicked should track on analytics`() {
        createViewModel()

        viewModel.onBackClicked()

        verify { mockAnalytics.trackBackClicked() }
    }

    @Test
    fun `onNativeBackClicked should track on analytics`() {
        createViewModel()

        viewModel.onNativeBackClicked()

        verify { mockAnalytics.trackNativeBackClicked() }
    }

    private fun createViewModel(isPickingPlayer: Boolean = false, shouldShowTip: Boolean = false) {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        every { mockPreferenceManager.shouldShowPlayerListTip() } returns shouldShowTip

        viewModel = PlayerListViewModel(
            isPickingPlayer = isPickingPlayer,
            useCases = PlayerListViewModel.UseCases(
                addOrUpdatePlayerUseCase = mockAddOrUpdatePlayerUseCase,
                addOrUpdatePlayerStatsUseCase = mockAddOrUpdatePlayerStatsUseCase,
                getPlayersUseCase = mockGetPlayersUseCase
            ),
            analytics = mockAnalytics,
            preferenceManager = mockPreferenceManager,
            logger = mockLogger,
            dispatcher = testCoroutineDispatcher
        ).apply {
            state.observeForever(mockStateObserver)
            action.observeForever(mockActionObserver)
        }
    }

}
