package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.usecase.match.AddMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test

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

    private lateinit var viewModel: NewMatchViewModel

    @Test
    fun onFieldErased() {
    }

    @Test
    fun onAllFieldsFilled() {
    }

    @Test
    fun onStartMatchButtonClicked() {
    }

    @Test
    fun onHelpButtonClicked() {
    }

    private fun createViewModel() {
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

}
