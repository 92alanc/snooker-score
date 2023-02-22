package com.alancamargo.snookerscore.features.match.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.databinding.ActivityMatchListBinding
import com.alancamargo.snookerscore.features.match.ui.adapter.MatchAdapter
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch
import com.alancamargo.snookerscore.features.match.ui.viewmodel.list.MatchListUiAction
import com.alancamargo.snookerscore.features.match.ui.viewmodel.list.MatchListUiState
import com.alancamargo.snookerscore.features.match.ui.viewmodel.list.MatchListViewModel
import com.alancamargo.snookerscore.navigation.MatchDetailsNavigation
import com.alancamargo.snookerscore.navigation.NewMatchNavigation
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchListActivity : AppCompatActivity() {

    private var _binding: ActivityMatchListBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { MatchAdapter(viewModel::onMatchClicked) }
    private val viewModel by viewModel<MatchListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMatchListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()

        observeState(viewModel, ::onStateChanged)
        observeAction(viewModel, ::onAction)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMatches()
    }

    override fun onBackPressed() {
        viewModel.onNativeBackClicked()
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onBackClicked()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.banner.destroy()
    }

    private fun setUpUi() = with(binding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.adapter = adapter
        btNewMatch.setOnClickListener { viewModel.onNewMatchClicked() }
        get<AdLoader>().loadBannerAds(banner)
    }

    private fun onStateChanged(state: MatchListUiState) {
        state.matches?.let(adapter::submitList)
    }

    private fun onAction(action: MatchListUiAction) {
        when (action) {
            is MatchListUiAction.ShowError -> showError()
            is MatchListUiAction.OpenNewMatch -> openNewMatch()
            is MatchListUiAction.OpenMatchDetails -> openMatchDetails(action.match)
            is MatchListUiAction.Finish -> finish()
        }
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getMatches()
            }.show()
    }

    private fun openNewMatch() {
        val navigation = get<NewMatchNavigation>()
        navigation.startActivity(context = this)

        finish()
    }

    private fun openMatchDetails(match: UiMatch) {
        val navigation = get<MatchDetailsNavigation>()
        navigation.startActivity(context = this, match = match)
    }

    companion object {
        fun getIntent(context: Context): Intent = context.createIntent<MatchListActivity>()
    }

}
