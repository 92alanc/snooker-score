package com.alancamargo.snookerscore.features.match.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.core.ui.AdLoader
import com.alancamargo.snookerscore.core.ui.button
import com.alancamargo.snookerscore.core.ui.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityMatchDetailsBinding
import com.alancamargo.snookerscore.navigation.MatchSummaryNavigation
import com.alancamargo.snookerscore.ui.adapter.frame.FrameAdapter
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch
import com.alancamargo.snookerscore.features.match.ui.viewmodel.details.MatchDetailsUiAction
import com.alancamargo.snookerscore.features.match.ui.viewmodel.details.MatchDetailsUiState
import com.alancamargo.snookerscore.features.match.ui.viewmodel.details.MatchDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DIALOGUE_TAG = "DialogueTag"

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMatchDetails(args.match)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.banner.destroy()
    }

    private fun setUpUi() = with(binding) {
        setUpToolbar()
        setUpButtons()
        recyclerView.adapter = adapter
        get<AdLoader>().loadBannerAds(banner, R.string.ads_match_details)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpButtons() = with(binding) {
        btDeleteMatch.setOnClickListener { viewModel.onDeleteMatchClicked() }
        btViewSummary.setOnClickListener { viewModel.onViewSummaryClicked() }
    }

    private fun onStateChanged(state: MatchDetailsUiState) = with(state) {
        handleFrames()
        handleWinner()
    }

    private fun onAction(action: MatchDetailsUiAction) {
        when (action) {
            is MatchDetailsUiAction.ShowError -> showError()
            is MatchDetailsUiAction.Finish -> finish()
            is MatchDetailsUiAction.ShowDeleteMatchConfirmation -> showDeleteMatchConfirmation()
            is MatchDetailsUiAction.ViewSummary -> viewSummary(action.frames)
        }
    }

    private fun MatchDetailsUiState.handleFrames() {
        frames?.let(adapter::submitList)
    }

    private fun MatchDetailsUiState.handleWinner() {
        binding.txtWinner.text = winner?.let { winner ->
            getString(R.string.winner_format, winner.name)
        }
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getMatchDetails(args.match)
            }.show()
    }

    private fun showDeleteMatchConfirmation() {
        makeDialogue {
            titleRes = R.string.delete_match
            messageRes = R.string.delete_match_confirmation
            illustrationRes = R.drawable.ic_warning
            primaryButton = button {
                textRes = R.string.cancel
            }
            secondaryButton = button {
                textRes = R.string.yes
                onClick = { viewModel.onDeleteMatchConfirmed(args.match) }
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun viewSummary(frames: List<UiFrame>) {
        val navigation = get<MatchSummaryNavigation>()
        navigation.startActivity(context = this, frames = frames)

        finish()
    }

    @Parcelize
    data class Args(val match: UiMatch) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<MatchDetailsActivity>().putArguments(args)
        }
    }

}
