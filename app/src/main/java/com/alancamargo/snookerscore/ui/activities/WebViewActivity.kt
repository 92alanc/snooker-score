package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.webkit.WebViewClient
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.databinding.ActivityWebViewBinding
import kotlinx.parcelize.Parcelize

class WebViewActivity : AppCompatActivity() {

    private var _binding: ActivityWebViewBinding? = null
    private val binding
        get() = _binding!!

    private val args by args<Args>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() {
        setUpToolbar()
        setUpWebView()
    }

    private fun setUpToolbar() {
        binding.toolbar.setTitle(args.titleRes)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Suppress("SetJavaScriptEnabled")
    private fun setUpWebView() {
        with(binding.webView) {
            webViewClient = WebViewClient()
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
