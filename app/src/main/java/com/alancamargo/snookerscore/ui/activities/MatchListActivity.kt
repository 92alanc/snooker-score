package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.ui.formatDateTime
import com.alancamargo.snookerscore.databinding.ActivityMatchListBinding
import com.alancamargo.snookerscore.ui.adapter.match.MatchAdapter
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListUiAction
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListUiState
import com.alancamargo.snookerscore.ui.viewmodel.matchlist.MatchListViewModel
import com.google.android.material.snackbar.Snackbar
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

        viewModel.getMatches()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() = with(binding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.adapter = adapter
        btNewMatch.setOnClickListener { viewModel.onNewMatchClicked() }
    }

    private fun onStateChanged(state: MatchListUiState) {
        state.matches?.let(adapter::submitList)
    }

    private fun onAction(action: MatchListUiAction) {
        when (action) {
            is MatchListUiAction.ShowLoading -> showLoading()
            is MatchListUiAction.HideLoading -> hideLoading()
            is MatchListUiAction.ShowError -> showError()
            is MatchListUiAction.OpenNewMatch -> openNewMatch()
            is MatchListUiAction.OpenMatchDetails -> openMatchDetails(action.match)
        }
    }

    private fun showLoading() = with(binding) {
        groupContent.isVisible = false
        progressBar.isVisible = true
    }

    private fun hideLoading() = with(binding) {
        progressBar.isVisible = false
        groupContent.isVisible = true
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getMatches()
            }.show()
    }

    private fun openNewMatch() {
        // TODO
        Toast.makeText(this, "New match", Toast.LENGTH_SHORT).show()
    }

    private fun openMatchDetails(match: UiMatch) {
        // TODO
        Toast.makeText(this, match.dateTime.formatDateTime(), Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun getIntent(context: Context): Intent = context.createIntent<MatchListActivity>()
    }

}