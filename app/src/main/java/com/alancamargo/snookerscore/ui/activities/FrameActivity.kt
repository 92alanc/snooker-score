package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.core.ui.button
import com.alancamargo.snookerscore.core.ui.makeDialogue
import com.alancamargo.snookerscore.core.ui.radioButton
import com.alancamargo.snookerscore.core.ui.radioButtons
import com.alancamargo.snookerscore.databinding.ActivityFrameBinding
import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.navigation.MainNavigation
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiMatch
import com.alancamargo.snookerscore.ui.model.UiPlayer
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameUiAction
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameUiState
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameViewModel
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
        viewModel.onForfeitMatchClicked()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() {
        setUpToolbar()
        setUpButtons()
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
            is FrameUiAction.ShowLoading -> showLoading()
            is FrameUiAction.HideLoading -> hideLoading()
            is FrameUiAction.ShowError -> showError()
            is FrameUiAction.ShowObjectBalls -> showObjectBalls()
            is FrameUiAction.OpenMain -> openMain()
            is FrameUiAction.ShowEndFrameConfirmation -> showEndFrameConfirmation()
            is FrameUiAction.ShowForfeitMatchConfirmation -> showForfeitMatchConfirmation()
            is FrameUiAction.OpenMatchSummary -> openMatchSummary(action.match)
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

        btUndoPottedBall.setOnClickListener { viewModel.onUndoLastPottedBallClicked() }
        btUndoFoul.setOnClickListener { viewModel.onUndoLastFoulClicked() }
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
        setOnClickListener { viewModel.onBallPotted(ball) }
    }

    private fun showStartingPlayerPrompt(player1: UiPlayer, player2: UiPlayer) {
        makeDialogue {
            titleRes = R.string.title_select_player
            messageRes = R.string.message_select_player
            radioButtons = radioButtons {
                radioButton {
                    id = player1.hashCode()
                    text = player1.name
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

    private fun showLoading() = with(binding) {
        groupContent.isVisible = false
        progressBar.isVisible = true
    }

    private fun hideLoading() = with(binding) {
        progressBar.isVisible = false
        groupContent.isVisible = true
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
                    val ball = Ball.values()[ballOrdinal]
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
            }
        }.show(supportFragmentManager, DIALOGUE_TAG)
    }

    private fun openMatchSummary(match: UiMatch) {
        // TODO
        Toast.makeText(this, "Match ended", Toast.LENGTH_SHORT).show()
    }

    @Parcelize
    data class Args(val frames: List<UiFrame>) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<FrameActivity>().putArguments(args)
        }
    }

}
