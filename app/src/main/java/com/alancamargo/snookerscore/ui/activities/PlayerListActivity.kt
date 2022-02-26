package com.alancamargo.snookerscore.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.arch.extensions.createIntent
import com.alancamargo.snookerscore.core.arch.extensions.observeAction
import com.alancamargo.snookerscore.core.arch.extensions.observeState
import com.alancamargo.snookerscore.databinding.ActivityPlayerListBinding
import com.alancamargo.snookerscore.ui.adapter.PlayerAdapter
import com.alancamargo.snookerscore.ui.model.UiPlayer
import com.alancamargo.snookerscore.ui.viewmodel.playerlist.PlayerListUiAction
import com.alancamargo.snookerscore.ui.viewmodel.playerlist.PlayerListUiState
import com.alancamargo.snookerscore.ui.viewmodel.playerlist.PlayerListViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerListActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerListBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { PlayerAdapter(viewModel::onPlayerSelected) }
    private val viewModel by viewModel<PlayerListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUi()

        observeState(viewModel, ::handleState)
        observeAction(viewModel, ::handleAction)
        viewModel.getPlayers()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpUi() = with(binding) {
        setUpToolbar()
        recyclerView.adapter = adapter
        btAddPlayer.setOnClickListener { viewModel.onNewPlayerClicked() }
    }

    private fun handleState(state: PlayerListUiState) {
        state.players?.let(adapter::submitList)
    }

    private fun handleAction(action: PlayerListUiAction) {
        when (action) {
            is PlayerListUiAction.ShowLoading -> showLoading()
            is PlayerListUiAction.HideLoading -> hideLoading()
            is PlayerListUiAction.ShowError -> showError()
            is PlayerListUiAction.ShowNewPlayerDialogue -> showNewPlayerDialogue()
            is PlayerListUiAction.SelectPlayer -> showPlayerDetails(action.player)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading() = with(binding) {
        recyclerView.isVisible = false
        progressBar.isVisible = true
    }

    private fun hideLoading() = with(binding) {
        progressBar.isVisible = false
        recyclerView.isVisible = true
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_SHORT)
            .setAction(R.string.retry) {
                viewModel.getPlayers()
            }.show()
    }

    private fun showNewPlayerDialogue() {
        // TODO
        Toast.makeText(this, "New player", Toast.LENGTH_SHORT).show()
    }

    private fun showPlayerDetails(player: UiPlayer) {
        // TODO
        Toast.makeText(this, player.name, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun getIntent(context: Context): Intent = context.createIntent<PlayerListActivity>()
    }

}
