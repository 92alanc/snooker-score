package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.databinding.ActivityFrameBinding
import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameUiAction
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameUiState
import com.alancamargo.snookerscore.ui.viewmodel.frame.FrameViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

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

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onForfeitMatchClicked()
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

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpButtons() = with(binding) {
        bindButtonToBall(btRed, Ball.RED)
        bindButtonToBall(btYellow, Ball.YELLOW)
        bindButtonToBall(btGreen, Ball.GREEN)
        bindButtonToBall(btBrown, Ball.BROWN)
        bindButtonToBall(btBlue, Ball.BLUE)
        bindButtonToBall(btPink, Ball.PINK)
        bindButtonToBall(btBlack, Ball.BLACK)

        btUndoPottedBall.setOnClickListener { viewModel.onUndoLastPottedBallClicked() }
        btUndoFoul.setOnClickListener { viewModel.onUndoLastFoulClicked() }
        btEndTurn.setOnClickListener { viewModel.onEndTurnClicked() }
        btEndFrame.setOnClickListener { viewModel.onEndFrameConfirmed() }
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

    private fun bindButtonToBall(button: MaterialButton, ball: Ball) {
        button.setOnClickListener { viewModel.onBallPotted(ball) }
    }

    @Parcelize
    data class Args(val frames: List<UiFrame>) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<FrameActivity>().putArguments(args)
        }
    }

}
