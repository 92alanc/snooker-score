package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.databinding.ActivityWebViewBinding
import com.alancamargo.snookerscore.ui.viewmodel.webview.WebViewUiAction
import com.alancamargo.snookerscore.ui.viewmodel.webview.WebViewViewModel
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewActivity : AppCompatActivity() {

    private var _binding: ActivityWebViewBinding? = null
    private val binding
        get() = _binding!!

    private val args by args<Args>()
    private val viewModel by viewModel<WebViewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        observeAction(viewModel, ::onActionChanged)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() {
        setUpToolbar()
        setUpWebView()
    }

    private fun onActionChanged(action: WebViewUiAction) {
        when (action) {
            WebViewUiAction.ShowLoading -> binding.progressBar.isVisible = true
            WebViewUiAction.RenderPage -> renderPage()
            WebViewUiAction.Finish -> finish()
            WebViewUiAction.ShowError -> showError()
            WebViewUiAction.Reload -> binding.webView.loadUrl(args.url)
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.setTitle(args.titleRes)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Suppress("SetJavaScriptEnabled")
    private fun setUpWebView() {
        with(binding.webView) {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    viewModel.onStartLoading()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    viewModel.onFinishedLoading()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    viewModel.onError()
                }
            }
            settings.javaScriptEnabled = true
            loadUrl(args.url)
        }
    }

    private fun renderPage() = with(binding) {
        progressBar.isVisible = false
        webView.isVisible = true
    }

    private fun showError() = with(binding) {
        progressBar.isVisible = false
        webView.isVisible = false
        // TODO: show error
    }

    @Parcelize
    data class Args(@StringRes val titleRes: Int, val url: String) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<WebViewActivity>().putArguments(args)
        }
    }

}
