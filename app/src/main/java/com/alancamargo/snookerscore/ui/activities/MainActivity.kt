package com.alancamargo.snookerscore.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.databinding.ActivityMainBinding
import com.alancamargo.snookerscore.navigation.WebViewNavigation
import com.alancamargo.snookerscore.ui.viewmodel.main.MainUiAction
import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        // TODO
        Toast.makeText(this, "Matches", Toast.LENGTH_SHORT).show()
    }

    private fun openPlayers() {
        // TODO
        Toast.makeText(this, "Players", Toast.LENGTH_SHORT).show()
    }

    private fun openRules(titleRes: Int, url: String) {
        val navigation = get<WebViewNavigation>()
        navigation.startActivity(context = this, titleRes = titleRes, url = url)
    }

    private fun showAppInfo() {
        // TODO
        Toast.makeText(this, "App info", Toast.LENGTH_SHORT).show()
    }

}
