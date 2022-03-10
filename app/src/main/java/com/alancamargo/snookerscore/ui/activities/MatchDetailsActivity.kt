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
import com.alancamargo.snookerscore.core.ui.button
import com.alancamargo.snookerscore.core.ui.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityMatchDetailsBinding
import com.alancamargo.snookerscore.navigation.FrameNavigation
import com.alancamargo.snookerscore.ui.adapter.frame.FrameAdapter
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsUiAction
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsUiState
import com.alancamargo.snookerscore.ui.viewmodel.match.MatchDetailsViewModel
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

    private fun setUpUi() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView.adapter = adapter
        btDeleteMatch.setOnClickListener { viewModel.onDeleteMatchClicked() }
        btResumeMatch.setOnClickListener { viewModel.onResumeMatchClicked() }
    }

    private fun onStateChanged(state: MatchDetailsUiState) = with(state) {
        handleFrames()
        handleWinner()
        handleResumeMatchButton()
    }

    private fun onAction(action: MatchDetailsUiAction) {
        when (action) {
            is MatchDetailsUiAction.ShowLoading -> showLoading()
            is MatchDetailsUiAction.HideLoading -> hideLoading()
            is MatchDetailsUiAction.ShowError -> showError()
            is MatchDetailsUiAction.Finish -> finish()
            is MatchDetailsUiAction.ShowDeleteMatchConfirmation -> showDeleteMatchConfirmation()
            is MatchDetailsUiAction.ResumeMatch -> resumeMatch(action.frames)
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

    private fun MatchDetailsUiState.handleResumeMatchButton() {
        binding.btResumeMatch.isVisible = isResumeMatchButtonEnabled
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

    private fun resumeMatch(frames: List<UiFrame>) {
        val navigation = get<FrameNavigation>()
        navigation.startActivity(context = this, frames = frames)
    }

    @Parcelize
    data class Args(val match: UiMatch) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<MatchDetailsActivity>().putArguments(args)
        }
    }

}
