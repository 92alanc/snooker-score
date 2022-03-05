package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.databinding.ActivityFrameBinding
import com.alancamargo.snookerscore.domain.model.Ball
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
        setUpBallButtons()
    }

    private fun onStateChanged(state: FrameUiState) = with(state) {

    }

    private fun onAction(action: FrameUiAction) {

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpBallButtons() = with(binding) {
        bindButtonToBall(btRed, Ball.RED)
        bindButtonToBall(btYellow, Ball.YELLOW)
        bindButtonToBall(btGreen, Ball.GREEN)
        bindButtonToBall(btBrown, Ball.BROWN)
        bindButtonToBall(btBlue, Ball.BLUE)
        bindButtonToBall(btPink, Ball.PINK)
        bindButtonToBall(btBlack, Ball.BLACK)
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
