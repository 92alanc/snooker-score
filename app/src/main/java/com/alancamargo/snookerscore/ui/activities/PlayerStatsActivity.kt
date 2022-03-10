package com.alancamargo.snookerscore.ui.activities

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
import com.alancamargo.snookerscore.core.ui.button
import com.alancamargo.snookerscore.core.ui.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityPlayerStatsBinding
import com.alancamargo.snookerscore.ui.model.UiPlayer
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsUiAction
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsUiState
import com.alancamargo.snookerscore.ui.viewmodel.playerstats.PlayerStatsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DIALOGUE_TAG = "DialogueTag"

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
            binding.toolbar.title = player.name
            binding.txtMatchesWon.text = matchesWon.toString()
            binding.txtHighestScore.text = highestScore.toString()
            binding.txtHighestBreak.text = highestBreak.toString()
            binding.btDeletePlayer.setOnClickListener { viewModel.onDeletePlayerClicked() }
        }
    }

    private fun onAction(action: PlayerStatsUiAction) {
        when (action) {
            PlayerStatsUiAction.ShowError -> showError()
            PlayerStatsUiAction.Finish -> finish()
            PlayerStatsUiAction.ShowDeletePlayerConfirmation -> showDeletePlayerConfirmation()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getPlayerStats(args.player)
            }.show()
    }

    private fun showDeletePlayerConfirmation() {
        makeDialogue {
            titleRes = R.string.delete_player
            messageRes = R.string.delete_player_confirmation
            illustrationRes = R.drawable.ic_warning
            primaryButton = button {
                textRes = R.string.cancel
            }
            secondaryButton = button {
                textRes = R.string.yes
                onClick = { viewModel.onDeletePlayerConfirmed(args.player) }
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    @Parcelize
    data class Args(val player: UiPlayer) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<PlayerStatsActivity>().putArguments(args)
        }
    }

}
