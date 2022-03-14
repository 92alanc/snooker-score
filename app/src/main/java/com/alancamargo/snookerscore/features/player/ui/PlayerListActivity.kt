package com.alancamargo.snookerscore.features.player.ui

import android.app.Activity
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
import com.alancamargo.snookerscore.core.arch.extensions.putResponse
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.core.ui.dialogue.button
import com.alancamargo.snookerscore.core.ui.dialogue.editText
import com.alancamargo.snookerscore.core.ui.dialogue.makeDialogue
import com.alancamargo.snookerscore.core.ui.dialogue.radioButton
import com.alancamargo.snookerscore.core.ui.dialogue.radioButtons
import com.alancamargo.snookerscore.databinding.ActivityPlayerListBinding
import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.features.player.ui.viewmodel.PlayerListUiAction
import com.alancamargo.snookerscore.features.player.ui.viewmodel.PlayerListUiState
import com.alancamargo.snookerscore.features.player.ui.viewmodel.PlayerListViewModel
import com.alancamargo.snookerscore.navigation.PlayerStatsNavigation
import com.alancamargo.snookerscore.features.player.ui.adapter.PlayerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val DIALOGUE_TAG = "DialogueTag"

class PlayerListActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerListBinding? = null
    private val binding get() = _binding!!

    private val args by args<Args>()
    private val adapter by lazy {
        PlayerAdapter(viewModel::onPlayerClicked, viewModel::onPlayerLongClicked)
    }
    private val viewModel by viewModel<PlayerListViewModel> { parametersOf(args.isPickingPlayer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()

        observeState(viewModel, ::handleState)
        observeAction(viewModel, ::handleAction)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlayers()
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
        recyclerView.adapter = adapter
        btAddPlayer.setOnClickListener { viewModel.onNewPlayerClicked() }
        get<AdLoader>().loadBannerAds(banner, R.string.ads_player_list)
    }

    private fun handleState(state: PlayerListUiState) {
        state.players?.let(adapter::submitList)
    }

    private fun handleAction(action: PlayerListUiAction) {
        when (action) {
            is PlayerListUiAction.ShowError -> showError()
            is PlayerListUiAction.ShowNewPlayerDialogue -> showNewPlayerDialogue()
            is PlayerListUiAction.OpenPlayerStats -> showPlayerStats(action.player)
            is PlayerListUiAction.PickPlayer -> pickPlayer(action.player)
            is PlayerListUiAction.EditPlayer -> editPlayer(action.player)
            is PlayerListUiAction.ShowTip -> showTip()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.getPlayers()
            }.show()
    }

    private fun showNewPlayerDialogue() {
        makeDialogue {
            titleRes = R.string.add_player
            editText = editText {
                hintRes = R.string.name
                onSubmitText = viewModel::setPlayerName
            }
            radioButtons = radioButtons {
                radioButton {
                    id = Gender.MALE.ordinal
                    textRes = R.string.male
                }
                radioButton {
                    id = Gender.FEMALE.ordinal
                    textRes = R.string.female
                }
                onSubmitSelection = viewModel::setPlayerGenderOrdinal
            }
            primaryButton = button {
                textRes = R.string.save
                onClick = viewModel::onSavePlayerClicked
            }
            secondaryButton = button {
                textRes = R.string.cancel
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun showPlayerStats(player: UiPlayer) {
        val navigation = get<PlayerStatsNavigation>()
        navigation.startActivity(context = this, player = player)
    }

    private fun pickPlayer(player: UiPlayer) {
        val response = PlayerResponse(player)
        val intent = Intent().putResponse(response)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun editPlayer(player: UiPlayer) {
        makeDialogue {
            titleRes = R.string.edit_player
            editText = editText {
                hintRes = R.string.name
                text = player.name
                onSubmitText = viewModel::setPlayerName
            }
            radioButtons = radioButtons {
                radioButton {
                    id = Gender.MALE.ordinal
                    textRes = R.string.male
                    isChecked = id == player.gender.ordinal
                }
                radioButton {
                    id = Gender.FEMALE.ordinal
                    textRes = R.string.female
                    isChecked = id == player.gender.ordinal
                }
                onSubmitSelection = viewModel::setPlayerGenderOrdinal
            }
            primaryButton = button {
                textRes = R.string.save
                onClick = viewModel::onSavePlayerClicked
            }
            secondaryButton = button {
                textRes = R.string.cancel
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun showTip() {
        makeDialogue {
            titleRes = R.string.tip
            messageRes = R.string.tip_player_list_message
            illustrationRes = R.drawable.illu_tip
            primaryButton = button {
                textRes = R.string.ok
            }
            secondaryButton = button {
                textRes = R.string.dont_show_again
                onClick = viewModel::onDontShowTipAgainClicked
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    @Parcelize
    data class Args(val isPickingPlayer: Boolean) : Parcelable

    @Parcelize
    data class PlayerResponse(val player: UiPlayer) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<PlayerListActivity>().putArguments(args)
        }
    }

}
