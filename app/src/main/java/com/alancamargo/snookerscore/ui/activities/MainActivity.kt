package com.alancamargo.snookerscore.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.databinding.ActivityMainBinding
import com.alancamargo.snookerscore.ui.viewmodel.main.MainUiAction
import com.alancamargo.snookerscore.ui.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        observeAction(viewModel, ::handleAction)
    }

    private fun setUpUi() = with(binding) {
        btMatches.setOnClickListener { viewModel.onClickMatches() }
        btPlayers.setOnClickListener { viewModel.onClickPlayers() }
        btRules.setOnClickListener { viewModel.onClickRules() }
        btAbout.setOnClickListener { viewModel.onClickAbout() }
    }

    private fun handleAction(action: MainUiAction) {
        when (action) {
            is MainUiAction.OpenMatches -> openMatches()
            is MainUiAction.OpenPlayers -> openPlayers()
            is MainUiAction.OpenRules -> openRules(action.url)
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

    private fun openRules(url: String) {
        // TODO
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
    }

    private fun showAppInfo() {
        // TODO
        Toast.makeText(this, "App info", Toast.LENGTH_SHORT).show()
    }

}
