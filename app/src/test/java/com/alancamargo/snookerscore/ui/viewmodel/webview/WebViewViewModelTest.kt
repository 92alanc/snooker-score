package com.alancamargo.snookerscore.ui.viewmodel.webview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alancamargo.snookerscore.features.webview.ui.viewmodel.WebViewUiAction
import com.alancamargo.snookerscore.features.webview.ui.viewmodel.WebViewViewModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WebViewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockActionObserver = mockk<Observer<WebViewUiAction>>(relaxed = true)

    private lateinit var viewModel: WebViewViewModel

    @Before
    fun setUp() {
        viewModel = WebViewViewModel().apply {
            action.observeForever(mockActionObserver)
        }
    }

    @Test
    fun `onStartLoading should send ShowLoading action`() {
        viewModel.onStartLoading()

        verify { mockActionObserver.onChanged(WebViewUiAction.ShowLoading) }
    }

    @Test
    fun `onFinishedLoading should send HideLoading action`() {
        viewModel.onFinishedLoading()

        verify { mockActionObserver.onChanged(WebViewUiAction.HideLoading) }
    }

    @Test
    fun `onRefresh should send Refresh action`() {
        viewModel.onRefresh()

        verify { mockActionObserver.onChanged(WebViewUiAction.Refresh) }
    }

}
