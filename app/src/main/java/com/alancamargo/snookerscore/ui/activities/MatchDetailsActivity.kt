package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.databinding.ActivityMatchDetailsBinding
import com.alancamargo.snookerscore.ui.adapter.frame.FrameAdapter
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsUiAction
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsUiState
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityMatchDetailsBinding? = null
    private val binding get() = _binding!!

    private val adapter = FrameAdapter()

    private val args by args<Args>()
    private val viewModel by viewModel<MatchDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMatchDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()

        observeState(viewModel, ::onStateChanged)
        observeAction(viewModel, ::onAction)

        viewModel.getMatchDetails(args.match)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.adapter = adapter
        btDeleteMatch.setOnClickListener { viewModel.onDeleteMatchClicked(args.match) }
    }

    private fun onStateChanged(state: MatchDetailsUiState) = with(state) {
        handleFrames()
        handleWinner()
    }

    private fun onAction(action: MatchDetailsUiAction) {
        when (action) {
            MatchDetailsUiAction.ShowLoading -> showLoading()
            MatchDetailsUiAction.HideLoading -> hideLoading()
            MatchDetailsUiAction.ShowError -> showError()
            MatchDetailsUiAction.Finish -> finish()
        }
    }

    private fun MatchDetailsUiState.handleFrames() {
        frames?.let(adapter::submitList)
    }

    private fun MatchDetailsUiState.handleWinner() {
        binding.txtWinner.text = winner?.let { winner ->
            getString(R.string.winner_format, winner.name)
        } ?: getString(R.string.unfinished_match)
    }

    private fun showLoading() = with(binding) {
        recyclerView.isVisible = false
        progressBar.isVisible = true
    }

    private fun hideLoading() = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = true
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getMatchDetails(args.match)
            }.show()
    }

    @Parcelize
    data class Args(val match: UiMatch) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<MatchDetailsActivity>().putArguments(args)
        }
    }

}
