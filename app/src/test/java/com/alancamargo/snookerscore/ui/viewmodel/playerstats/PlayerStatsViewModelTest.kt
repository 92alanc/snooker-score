package com.alancamargo.snookerscore.ui.viewmodel.playerstats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.usecase.player.DeletePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.testtools.getPlayer
import com.alancamargo.snookerscore.testtools.getPlayerStats
import com.alancamargo.snookerscore.ui.mapping.toUi
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
class PlayerStatsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetPlayerStatsUseCase = mockk<GetPlayerStatsUseCase>()
    private val mockDeletePlayerUseCase = mockk<DeletePlayerUseCase>()
    private val mockLogger = mockk<Logger>(relaxed = true)
    private val mockStateObserver = mockk<Observer<PlayerStatsUiState>>(relaxed = true)
    private val mockActionObserver = mockk<Observer<PlayerStatsUiAction>>(relaxed = true)

    private val player = getPlayer().toUi()

    private lateinit var viewModel: PlayerStatsViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = PlayerStatsViewModel(
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
    fun `onDeletePlayerClicked should send ShowDeletePlayerConfirmation`() {
        viewModel.onDeletePlayerClicked()

        verify { mockActionObserver.onChanged(PlayerStatsUiAction.ShowDeletePlayerConfirmation) }
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

}
