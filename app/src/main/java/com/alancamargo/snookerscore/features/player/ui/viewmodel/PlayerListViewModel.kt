package com.alancamargo.snookerscore.features.player.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.core.preferences.PreferenceManager
import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.domain.usecase.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.features.player.domain.usecase.GetPlayersUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toUi
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.AddOrUpdatePlayerStatsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.UUID

class PlayerListViewModel(
    private val isPickingPlayer: Boolean,
    private val addOrUpdatePlayerUseCase: AddOrUpdatePlayerUseCase,
    private val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val preferenceManager: PreferenceManager,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerListUiState, PlayerListUiAction>(initialState = PlayerListUiState()) {

    private var playerId = ""
    private var playerName = ""
    private var playerGenderOrdinal = -1

    fun onDontShowTipAgainClicked() {
        preferenceManager.dontShowPlayerListTipAgain()
    }

    fun onNewPlayerClicked() {
        playerId = UUID.randomUUID().toString()
        sendAction { PlayerListUiAction.ShowNewPlayerDialogue }
    }

    fun onPlayerClicked(player: UiPlayer) {
        val action = if (isPickingPlayer) {
            PlayerListUiAction.PickPlayer(player)
        } else {
            PlayerListUiAction.OpenPlayerStats(player)
        }

        sendAction { action }
    }

    fun onPlayerLongClicked(player: UiPlayer) {
        playerId = player.id
        sendAction { PlayerListUiAction.EditPlayer(player) }
    }

    fun onSavePlayerClicked() {
        if (playerName.isBlank() || playerGenderOrdinal < 0) {
            sendAction { PlayerListUiAction.ShowError }
            return
        }

        val gender = Gender.values()[playerGenderOrdinal]
        val player = Player(id = playerId, name = playerName, gender = gender)

        viewModelScope.launch {
            addOrUpdatePlayerUseCase(player).handleFlow {
                viewModelScope.launch {
                    addOrUpdatePlayerStatsUseCase(player).handleFlow {
                        if (isPickingPlayer) {
                            sendAction { PlayerListUiAction.PickPlayer(player.toUi()) }
                        } else {
                            getPlayers()
                        }
                    }
                }
            }
        }
    }

    fun setPlayerName(name: String) {
        playerName = name
    }

    fun setPlayerGenderOrdinal(ordinal: Int) {
        playerGenderOrdinal = ordinal
    }

    fun getPlayers() {
        viewModelScope.launch {
            getPlayersUseCase().handleFlow {
                val players = it.map { domainPlayer -> domainPlayer.toUi() }
                setState { state -> state.onPlayersReceived(players) }

                if (players.isNotEmpty() && preferenceManager.shouldShowPlayerListTip()) {
                    sendAction { PlayerListUiAction.ShowTip }
                }
            }
        }
    }

    private suspend fun <T> Flow<T>.handleFlow(block: (T) -> Unit) {
        flowOn(dispatcher).catch { throwable ->
            logger.error(throwable)
            sendAction { PlayerListUiAction.ShowError }
        }.collect {
            block(it)
        }
    }

}
