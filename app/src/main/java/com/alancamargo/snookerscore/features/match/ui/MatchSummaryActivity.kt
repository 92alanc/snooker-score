package com.alancamargo.snookerscore.features.match.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.*
import com.alancamargo.snookerscore.core.ui.ads.AdLoader
import com.alancamargo.snookerscore.databinding.ActivityMatchSummaryBinding
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import com.alancamargo.snookerscore.features.match.ui.viewmodel.summary.MatchSummaryUiAction
import com.alancamargo.snookerscore.features.match.ui.viewmodel.summary.MatchSummaryUiState
import com.alancamargo.snookerscore.features.match.ui.viewmodel.summary.MatchSummaryViewModel
import com.alancamargo.snookerscore.navigation.MainNavigation
import com.alancamargo.snookerscore.navigation.NewMatchNavigation
import kotlinx.parcelize.Parcelize
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MatchSummaryActivity : AppCompatActivity() {

    private var _binding: ActivityMatchSummaryBinding? = null
    private val binding get() = _binding!!

    private val args by args<Args>()
    private val viewModel by viewModel<MatchSummaryViewModel> { parametersOf(args.frames) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMatchSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()

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
        setUpToolbar()
        btNewMatch.setOnClickListener { viewModel.onNewMatchButtonClicked() }
        get<AdLoader>().loadBannerAds(banner)
    }

    private fun onStateChanged(state: MatchSummaryUiState) {
        state.matchSummary?.run {
            binding.txtPlayer1.text = match.player1.name
            binding.txtPlayer2.text = match.player2.name

            binding.imgPlayer1Trophy.isVisible = winner == match.player1
            binding.imgPlayer2Trophy.isVisible = winner == match.player2

            binding.txtPlayer1Score.text = getString(R.string.score_format, player1Score)
            binding.txtPlayer2Score.text = getString(R.string.score_format, player2Score)

            binding.txtPlayer1HighestBreak.text = getString(
                R.string.highest_break_format,
                player1HighestBreak
            )
            binding.txtPlayer2HighestBreak.text = getString(
                R.string.highest_break_format,
                player2HighestBreak
            )
        }
    }

    private fun onAction(action: MatchSummaryUiAction) {
        when (action) {
            MatchSummaryUiAction.NewMatch -> openNewMatch()
            MatchSummaryUiAction.OpenMain -> openMain()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openNewMatch() {
        val navigation = get<NewMatchNavigation>()
        navigation.startActivity(context = this)

        finish()
    }

    private fun openMain() {
        val navigation = get<MainNavigation>()
        navigation.startActivity(context = this)

        finish()
    }

    @Parcelize
    data class Args(val frames: List<UiFrame>) : Parcelable

    companion object {
        fun getIntent(context: Context, args: Args): Intent {
            return context.createIntent<MatchSummaryActivity>().putArguments(args)
        }
    }

}
