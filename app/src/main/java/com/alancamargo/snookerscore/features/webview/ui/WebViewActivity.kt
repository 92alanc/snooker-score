package com.alancamargo.snookerscore.features.webview.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.databinding.ActivityWebViewBinding
import com.alancamargo.snookerscore.features.webview.ui.viewmodel.WebViewUiAction
import com.alancamargo.snookerscore.features.webview.ui.viewmodel.WebViewViewModel
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewActivity : AppCompatActivity() {

    private var _binding: ActivityWebViewBinding? = null
    private val binding get() = _binding!!

    private val args by args<Args>()
    private val viewModel by viewModel<WebViewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        observeAction(viewModel, ::onActionChanged)
    }

    override fun onBackPressed() {
        viewModel.onNativeBackClicked()
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onBackClicked()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemRefresh -> {
                viewModel.onRefresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.banner.destroy()
    }

    private fun setUpUi() {
        setUpToolbar()
        setUpWebView()
        get<AdLoader>().loadBannerAds(binding.banner)
    }

    private fun onActionChanged(action: WebViewUiAction) {
        when (action) {
            WebViewUiAction.ShowLoading -> binding.progressBar.isVisible = true
            WebViewUiAction.HideLoading -> binding.progressBar.isVisible = false
            WebViewUiAction.Refresh -> binding.webView.loadUrl(args.url)
            WebViewUiAction.Finish -> finish()
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
            }
            settings.javaScriptEnabled = true
            loadUrl(args.url)
        }
    }

    @Parcelize
    data class Args(@StringRes val titleRes: Int, val url: String) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<WebViewActivity>().putArguments(args)
        }
    }

}
