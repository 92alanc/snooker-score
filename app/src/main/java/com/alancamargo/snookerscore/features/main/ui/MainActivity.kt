package com.alancamargo.snookerscore.features.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.core.ui.dialogue.button
import com.alancamargo.snookerscore.core.ui.dialogue.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityMainBinding
import com.alancamargo.snookerscore.features.main.ui.viewmodel.MainUiAction
import com.alancamargo.snookerscore.features.main.ui.viewmodel.MainViewModel
import com.alancamargo.snookerscore.navigation.MatchListNavigation
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DIALOGUE_TAG = "DialogueTag"

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        observeAction(viewModel, ::handleAction)
    }

    override fun onBackPressed() {
        viewModel.onClickBack()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAbout -> {
                viewModel.onClickAbout()
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
        setUpButtons()
        get<AdLoader>().loadBannerAds(binding.banner, R.string.ads_main)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setUpButtons() = with(binding) {
        btMatches.setOnClickListener { viewModel.onClickMatches() }
        btPlayers.setOnClickListener { viewModel.onClickPlayers() }
        btRules.setOnClickListener { viewModel.onClickRules() }
    }

    private fun handleAction(action: MainUiAction) {
        when (action) {
            is MainUiAction.OpenMatches -> openMatches()
            is MainUiAction.OpenPlayers -> openPlayers()
            is MainUiAction.OpenRules -> openRules(
                titleRes = action.screenTitleRes,
                url = action.url
            )
            is MainUiAction.ShowAppInfo -> showAppInfo()
            is MainUiAction.Finish -> finish()
        }
    }

    private fun openMatches() {
        val navigation = get<MatchListNavigation>()
        navigation.startActivity(context = this)
    }

    private fun openPlayers() {
        val navigation = get<PlayerListNavigation>()
        navigation.startActivity(context = this)
    }

    private fun openRules(titleRes: Int, url: String) {
        val navigation = get<WebViewNavigation>()
        navigation.startActivity(context = this, titleRes = titleRes, url = url)
    }

    private fun showAppInfo() {
        makeDialogue {
            titleRes = R.string.about
            messageRes = R.string.app_info
            illustrationRes = R.drawable.illu_table
            primaryButton = button {
                textRes = R.string.ok
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    companion object {
        fun getIntent(context: Context): Intent = context.createIntent<MainActivity>()
    }

}
