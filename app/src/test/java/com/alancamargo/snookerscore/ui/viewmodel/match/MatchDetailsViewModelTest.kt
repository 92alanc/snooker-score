package com.alancamargo.snookerscore.ui.viewmodel.match

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.testtools.getUiMatch
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
class MatchDetailsViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockDeleteMatchUseCase = mockk<DeleteMatchUseCase>()
    private val mockActionObserver = mockk<Observer<MatchDetailsUiAction>>(relaxed = true)

    private lateinit var viewModel: MatchDetailsViewModel

    @Before
    fun setUp() {
        val testCoroutineDispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(testCoroutineDispatcher)

        viewModel = MatchDetailsViewModel(mockDeleteMatchUseCase, testCoroutineDispatcher).apply {
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `onDeleteMatchClicked should send ShowLoading action`() {
        every {
            mockDeleteMatchUseCase.invoke(match = any())
        } returns flow { delay(timeMillis = 50) }

        viewModel.onDeleteMatchClicked(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.ShowLoading) }
    }

    @Test
    fun `onDeleteMatchClicked should send Finish action`() {
        every { mockDeleteMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        viewModel.onDeleteMatchClicked(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.Finish) }
    }

    @Test
    fun `onDeleteMatchClicked should send HideLoading action`() {
        every { mockDeleteMatchUseCase.invoke(match = any()) } returns flow { emit(Unit) }

        viewModel.onDeleteMatchClicked(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.HideLoading) }
    }

    @Test
    fun `with error onDeleteMatchClicked should send ShowErrorAction`() {
        every { mockDeleteMatchUseCase.invoke(match = any()) } returns flow { throw IOException() }

        viewModel.onDeleteMatchClicked(getUiMatch())

        verify { mockActionObserver.onChanged(MatchDetailsUiAction.ShowError) }
    }

}
