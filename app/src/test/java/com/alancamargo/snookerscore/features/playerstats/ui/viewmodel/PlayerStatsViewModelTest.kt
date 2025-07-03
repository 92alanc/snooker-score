package com.alancamargo.snookerscore.features.playerstats.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.player.domain.usecase.DeletePlayerUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.features.playerstats.data.analytics.PlayerStatsAnalytics
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.features.playerstats.ui.mapping.toUi
import com.alancamargo.snookerscore.testtools.getPlayer
import com.alancamargo.snookerscore.testtools.getPlayerStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class PlayerStatsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAnalytics = mockk<PlayerStatsAnalytics>(relaxed = true)
    private val mockGetPlayerStatsUseCase = mockk<GetPlayerStatsUseCase>()
    private val mockDeletePlayerUseCase = mockk<DeletePlayerUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<PlayerStatsUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<PlayerStatsUiAction>>(relaxed = true)

    private val player = getPlayer().toUi()

    private lateinit var viewModel: PlayerStatsViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = PlayerStatsViewModel(
            mockAnalytics,
            mockGetPlayerStatsUseCase,
            mockDeletePlayerUseCase,
            mockLogger,
            testCoroutineDispatcher
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
    fun `getPlayerStats should set state with player stats`() {
        val playerStats = getPlayerStats()
        every {
            mockGetPlayerStatsUseCase.invoke(player = any())
        } returns flow { emit(playerStats) }

        viewModel.getPlayerStats(player)

        val expected = playerStats.toUi()
        verify { mockStateObserver.onChanged(PlayerStatsUiState(expected)) }
    }

    @Test
    fun `with error getPlayerStats should send ShowError action`() {
        every {
            mockGetPlayerStatsUseCase.invoke(player = any())
        } returns flow { throw IOException() }

        viewModel.getPlayerStats(player)

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.ShowError) }
    }

    @Test
    fun `onDeletePlayerClicked should track on analytics`() {
        viewModel.onDeletePlayerClicked()

        verify { mockAnalytics.trackDeletePlayerClicked() }
    }

    @Test
    fun `onDeletePlayerClicked should send ShowDeletePlayerConfirmation`() {
        viewModel.onDeletePlayerClicked()

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.ShowDeletePlayerConfirmation) }
    }

    @Test
    fun `onDeletePlayerCancelled should track on analytics`() {
        viewModel.onDeletePlayerCancelled()

        verify { mockAnalytics.trackDeletePlayerCancelled() }
    }

    @Test
    fun `onDeletePlayerConfirmed should track on analytics`() {
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerConfirmed(player)

        verify { mockAnalytics.trackDeletePlayerConfirmed() }
    }

    @Test
    fun `onDeletePlayerConfirmed should send Finish action`() {
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { emit(Unit) }

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerConfirmed(player)

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.Finish) }
    }

    @Test
    fun `with error onDeletePlayerConfirmed should send ShowError action`() {
        every { mockDeletePlayerUseCase.invoke(any()) } returns flow { throw IOException() }

        val player = getPlayer().toUi()
        viewModel.onDeletePlayerConfirmed(player)

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.ShowError) }
    }

    @Test
    fun `onBackClicked should track on analytics`() {
        viewModel.onBackClicked()

        verify { mockAnalytics.trackBackClicked() }
    }

    @Test
    fun `onBackClicked should send Finish action`() {
        viewModel.onBackClicked()

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.Finish) }
    }

    @Test
    fun `onNativeBackClicked should track on analytics`() {
        viewModel.onNativeBackClicked()

        verify { mockAnalytics.trackNativeBackClicked() }
    }

    @Test
    fun `onNativeBackClicked should send Finish action`() {
        viewModel.onNativeBackClicked()

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.Finish) }
    }

}
