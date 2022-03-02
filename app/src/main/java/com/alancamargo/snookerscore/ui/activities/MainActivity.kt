package com.alancamargo.snookerscore.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.ui.button
import com.alancamargo.snookerscore.core.ui.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityMainBinding
import com.alancamargo.snookerscore.navigation.MatchListNavigation
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import com.alancamargo.snookerscore.ui.viewmodel.main.MainUiAction
import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAbout -> {
                showAppInfo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpUi() {
        setUpToolbar()
        setUpButtons()
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
            primaryButton = button {
                textRes = R.string.ok
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

}
