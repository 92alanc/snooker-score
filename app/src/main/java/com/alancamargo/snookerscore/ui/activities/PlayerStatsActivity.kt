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
import com.alancamargo.snookerscore.databinding.ActivityPlayerStatsBinding
import com.alancamargo.snookerscore.ui.model.UiPlayer
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsUiAction
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsUiState
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerStatsActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerStatsBinding? = null
    private val binding get() = _binding!!

    private val args by args<Args>()
    private val viewModel by viewModel<PlayerStatsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        observeState(viewModel, ::onStateChanged)
        observeAction(viewModel, ::onAction)
        viewModel.getPlayerStats(args.player)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onStateChanged(state: PlayerStatsUiState) {
        state.playerStats?.run {
            binding.groupContent.isVisible = true

            binding.toolbar.title = player.name
            binding.txtMatchesWon.text = matchesWon.toString()
            binding.txtHighestScore.text = highestScore.toString()
            binding.txtHighestBreak.text = highestBreak.toString()
            binding.btDeletePlayer.setOnClickListener {
                viewModel.onDeletePlayerClicked(player)
            }
        }
    }

    private fun onAction(action: PlayerStatsUiAction) {
        when (action) {
            PlayerStatsUiAction.ShowLoading -> showLoading()
            PlayerStatsUiAction.HideLoading -> binding.progressBar.isVisible = false
            PlayerStatsUiAction.ShowError -> showError()
            PlayerStatsUiAction.Finish -> finish()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading() = with(binding) {
        groupContent.isVisible = false
        progressBar.isVisible = true
    }

    private fun showError() = with(binding) {
        groupContent.isVisible = false

        Snackbar.make(root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getPlayerStats(args.player)
            }.show()
    }

    @Parcelize
    data class Args(val player: UiPlayer) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<PlayerStatsActivity>().putArguments(args)
        }
    }

}
