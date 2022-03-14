package com.alancamargo.snookerscore.features.playerstats.ui

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
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.core.ui.dialogue.button
import com.alancamargo.snookerscore.core.ui.dialogue.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityPlayerStatsBinding
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.features.playerstats.ui.viewmodel.PlayerStatsUiAction
import com.alancamargo.snookerscore.features.playerstats.ui.viewmodel.PlayerStatsUiState
import com.alancamargo.snookerscore.features.playerstats.ui.viewmodel.PlayerStatsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.get
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

        setUpUi()

        observeState(viewModel, ::onStateChanged)
        observeAction(viewModel, ::onAction)

        viewModel.getPlayerStats(args.player)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.banner.destroy()
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

    private fun setUpUi() = with(binding) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        get<AdLoader>().loadBannerAds(banner, R.string.ads_player_stats)
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
