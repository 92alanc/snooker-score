package com.alancamargo.snookerscore.features.frame.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.*
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.core.ui.dialogue.button
import com.alancamargo.snookerscore.core.ui.dialogue.makeDialogue
import com.alancamargo.snookerscore.core.ui.dialogue.radioButton
import com.alancamargo.snookerscore.core.ui.dialogue.radioButtons
import com.alancamargo.snookerscore.databinding.ActivityFrameBinding
import com.alancamargo.snookerscore.features.frame.domain.model.Ball
import com.alancamargo.snookerscore.features.frame.domain.model.Foul
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.frame.ui.viewmodel.FrameUiAction
import com.alancamargo.snookerscore.features.frame.ui.viewmodel.FrameUiState
import com.alancamargo.snookerscore.features.frame.ui.viewmodel.FrameViewModel
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.navigation.MainNavigation
import com.alancamargo.snookerscore.navigation.MatchSummaryNavigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val DIALOGUE_TAG = "DialogueTag"

class FrameActivity : AppCompatActivity() {

    private var _binding: ActivityFrameBinding? = null
    private val binding get() = _binding!!

    private val args by args<Args>()
    private val viewModel by viewModel<FrameViewModel> { parametersOf(args.frames) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()

        observeState(viewModel, ::onStateChanged)
        observeAction(viewModel, ::onAction)
    }

    override fun onBackPressed() {
        viewModel.onNativeBackClicked()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.banner.destroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() {
        setUpToolbar()
        setUpButtons()
        get<AdLoader>().loadBannerAds(binding.banner)
    }

    private fun onStateChanged(state: FrameUiState) = with(state) {
        handleFrame()
        handleCurrentPlayer()
        handleUndoButtons()
        handleBreakAndScores()
    }

    private fun onAction(action: FrameUiAction) {
        when (action) {
            is FrameUiAction.ShowStartingPlayerPrompt -> showStartingPlayerPrompt(
                action.player1,
                action.player2
            )
            is FrameUiAction.ShowError -> showError()
            is FrameUiAction.ShowObjectBalls -> showObjectBalls()
            is FrameUiAction.OpenMain -> openMain()
            is FrameUiAction.ShowEndFrameConfirmation -> showEndFrameConfirmation()
            is FrameUiAction.ShowForfeitMatchConfirmation -> showForfeitMatchConfirmation()
            is FrameUiAction.OpenMatchSummary -> openMatchSummary(action.frames)
            is FrameUiAction.ShowFullScreenAds -> showFullScreenAds()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpButtons() = with(binding) {
        btRed.bindToBall(Ball.RED)
        btYellow.bindToBall(Ball.YELLOW)
        btGreen.bindToBall(Ball.GREEN)
        btBrown.bindToBall(Ball.BROWN)
        btBlue.bindToBall(Ball.BLUE)
        btPink.bindToBall(Ball.PINK)
        btBlack.bindToBall(Ball.BLACK)

        btUndoPottedBall.setOnClickListener {
            vibrate()
            viewModel.onUndoLastPottedBallClicked()
        }
        btUndoFoul.setOnClickListener {
            vibrate()
            viewModel.onUndoLastFoulClicked()
        }
        btEndTurn.setOnClickListener { viewModel.onEndTurnClicked() }
        btEndFrame.setOnClickListener { viewModel.onEndFrameClicked() }
        btWithObjectBall.setOnClickListener { viewModel.onObjectBallFoulClicked() }
        btOthers.setOnClickListener { viewModel.onFoul(Foul.Other) }
    }

    private fun FrameUiState.handleFrame() {
        currentFrame?.let { frame ->
            binding.toolbar.title = getString(
                R.string.frame_x_y_format,
                frame.positionInMatch,
                frame.match.numberOfFrames
            )

            binding.txtPlayer1.text = frame.match.player1.name
            binding.txtPlayer2.text = frame.match.player2.name
        }
    }

    private fun FrameUiState.handleCurrentPlayer() {
        currentPlayer?.let { player ->
            currentFrame?.let { frame ->
                val currentPlayerBackgroundColour = ContextCompat.getColor(
                    this@FrameActivity,
                    R.color.red
                )

                if (player == frame.match.player1) {
                    binding.txtPlayer1.setBackgroundColor(currentPlayerBackgroundColour)
                    binding.txtPlayer2.setBackgroundResource(R.drawable.shape_outline_rectangular)
                } else {
                    binding.txtPlayer2.setBackgroundColor(currentPlayerBackgroundColour)
                    binding.txtPlayer1.setBackgroundResource(R.drawable.shape_outline_rectangular)
                }
            }
        }
    }

    private fun FrameUiState.handleUndoButtons() {
        binding.btUndoPottedBall.isVisible = isUndoLastPottedBallButtonEnabled
        binding.btUndoFoul.isVisible = isUndoLastFoulButtonEnabled
    }

    private fun FrameUiState.handleBreakAndScores() {
        binding.txtBreak.text = getString(R.string.break_format, breakValue)
        binding.txtPlayer1Score.text = player1Score.toString()
        binding.txtPlayer2Score.text = player2Score.toString()
    }

    private fun MaterialButton.bindToBall(ball: Ball) {
        setOnClickListener {
            vibrate()
            viewModel.onBallPotted(ball)
        }
    }

    private fun showStartingPlayerPrompt(player1: UiPlayer, player2: UiPlayer) {
        makeDialogue {
            titleRes = R.string.title_select_player
            messageRes = R.string.message_select_player
            radioButtons = radioButtons {
                radioButton {
                    id = player1.hashCode()
                    text = player1.name
                    isChecked = true
                }
                radioButton {
                    id = player2.hashCode()
                    text = player2.name
                }
                onSubmitSelection = {
                    val player = if (it == player1.hashCode()) {
                        player1
                    } else {
                        player2
                    }

                    viewModel.onStartingPlayerSelected(player)
                }
            }

            primaryButton = button {
                textRes = R.string.ok
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_LONG).show()
    }

    private fun showObjectBalls() {
        makeDialogue {
            titleRes = R.string.select_ball
            radioButtons = radioButtons {
                radioButton {
                    id = Ball.RED.ordinal
                    textRes = R.string.red
                    isChecked = true
                }
                radioButton {
                    id = Ball.YELLOW.ordinal
                    textRes = R.string.yellow
                }
                radioButton {
                    id = Ball.GREEN.ordinal
                    textRes = R.string.green
                }
                radioButton {
                    id = Ball.BROWN.ordinal
                    textRes = R.string.brown
                }
                radioButton {
                    id = Ball.BLUE.ordinal
                    textRes = R.string.blue
                }
                radioButton {
                    id = Ball.PINK.ordinal
                    textRes = R.string.pink
                }
                radioButton {
                    id = Ball.BLACK.ordinal
                    textRes = R.string.black
                }

                onSubmitSelection = { ballOrdinal ->
                    val ball = Ball.entries[ballOrdinal]
                    viewModel.onFoul(Foul.WithObjectBall(ball))
                }

                primaryButton = button {
                    textRes = R.string.ok
                }

                secondaryButton = button {
                    textRes = R.string.cancel
                }
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun openMain() {
        val navigation = get<MainNavigation>()
        navigation.startActivity(context = this)

        finish()
    }

    private fun showEndFrameConfirmation() {
        makeDialogue {
            titleRes = R.string.end_frame
            messageRes = R.string.end_frame_confirmation
            primaryButton = button {
                textRes = R.string.yes
                onClick = viewModel::onEndFrameConfirmed
            }
            secondaryButton = button {
                textRes = R.string.cancel
                onClick = viewModel::onEndFrameCancelled
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun showForfeitMatchConfirmation() {
        makeDialogue {
            titleRes = R.string.forfeit_match
            messageRes = R.string.forfeit_match_confirmation
            primaryButton = button {
                textRes = R.string.yes
                onClick = viewModel::onForfeitMatchConfirmed
            }
            secondaryButton = button {
                textRes = R.string.cancel
                onClick = viewModel::onForfeitMatchCancelled
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun openMatchSummary(frames: List<UiFrame>) {
        val navigation = get<MatchSummaryNavigation>()
        navigation.startActivity(context = this, frames = frames)

        finish()
    }

    private fun showFullScreenAds() {
        get<AdLoader>().loadInterstitialAds(activity = this, adIdRes = R.string.ads_full_screen)
    }

    @Parcelize
    data class Args(val frames: List<UiFrame>) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<FrameActivity>().putArguments(args)
        }
    }

}
