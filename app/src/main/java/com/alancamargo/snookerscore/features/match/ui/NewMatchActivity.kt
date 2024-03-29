package com.alancamargo.snookerscore.features.match.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.getResponse
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.core.ui.dialogue.button
import com.alancamargo.snookerscore.core.ui.dialogue.makeDialogue
import com.alancamargo.snookerscore.databinding.ActivityNewMatchBinding
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch.NewMatchUiAction
import com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch.NewMatchUiState
import com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch.NewMatchViewModel
import com.alancamargo.snookerscore.features.player.ui.PlayerListActivity
import com.alancamargo.snookerscore.navigation.FrameNavigation
import com.alancamargo.snookerscore.navigation.PlayerListNavigation
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DIALOGUE_TAG = "DialogueTag"

class NewMatchActivity : AppCompatActivity() {

    private var _binding: ActivityNewMatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<NewMatchViewModel>()

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()
        setUpActivityResultLauncher()

        observeState(viewModel, ::onStateChanged)
        observeAction(viewModel, ::onAction)
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
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpButtons()
        get<AdLoader>().loadBannerAds(banner)
    }

    private fun setUpActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val response = result.data.getResponse<PlayerListActivity.PlayerResponse>()
                viewModel.onPlayerSelected(response.player)
            }
        }
    }

    private fun setUpButtons() = with(binding) {
        btPlayer1.setOnClickListener { viewModel.onSelectPlayer1ButtonClicked() }
        btPlayer2.setOnClickListener { viewModel.onSelectPlayer2ButtonClicked() }
        btStartMatch.setOnClickListener { viewModel.onStartMatchButtonClicked() }
        btUp.setOnClickListener { viewModel.onNumberOfFramesIncreased() }
        btDown.setOnClickListener { viewModel.onNumberOfFramesDecreased() }
    }

    private fun onStateChanged(state: NewMatchUiState) = with(state) {
        handlePlayer1()
        handlePlayer2()
        handleNumberOfFrames()
        handleStartMatchButton()
    }

    private fun onAction(action: NewMatchUiAction) {
        when (action) {
            is NewMatchUiAction.ShowError -> showError()
            is NewMatchUiAction.PickPlayer -> pickPlayer()
            is NewMatchUiAction.StartMatch -> startMatch(action.frames)
            is NewMatchUiAction.ShowSamePlayersDialogue -> showSamePlayersDialogue()
            is NewMatchUiAction.Finish -> finish()
        }
    }

    private fun NewMatchUiState.handlePlayer1() {
        player1?.let {
            binding.txtPlayer1Name.isVisible = true
            binding.txtPlayer1Name.text = it.name
            binding.btPlayer1.setText(R.string.change)
        } ?: run {
            binding.txtPlayer1Name.isVisible = false
            binding.btPlayer1.setText(R.string.select)
        }
    }

    private fun NewMatchUiState.handlePlayer2() {
        player2?.let {
            binding.txtPlayer2Name.isVisible = true
            binding.txtPlayer2Name.text = it.name
            binding.btPlayer2.setText(R.string.change)
        } ?: run {
            binding.txtPlayer2Name.isVisible = false
            binding.btPlayer2.setText(R.string.select)
        }
    }

    private fun NewMatchUiState.handleNumberOfFrames() {
        binding.txtNumberOfFrames.text = numberOfFrames.toString()
    }

    private fun NewMatchUiState.handleStartMatchButton() {
        binding.btStartMatch.isEnabled = isStartMatchButtonEnabled
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.onStartMatchButtonClicked()
            }.show()
    }

    private fun pickPlayer() {
        val navigation = get<PlayerListNavigation>()
        val intent = navigation.getIntentToPickPlayer(context = this)
        activityResultLauncher?.launch(intent)
    }

    private fun startMatch(frames: List<UiFrame>) {
        val navigation = get<FrameNavigation>()
        navigation.startActivity(context = this, frames = frames)

        finish()
    }

    private fun showSamePlayersDialogue() {
        makeDialogue {
            titleRes = R.string.error
            messageRes = R.string.message_same_player
            primaryButton = button {
                textRes = R.string.ok
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    companion object {
        fun getIntent(context: Context): Intent = context.createIntent<NewMatchActivity>()
    }

}
