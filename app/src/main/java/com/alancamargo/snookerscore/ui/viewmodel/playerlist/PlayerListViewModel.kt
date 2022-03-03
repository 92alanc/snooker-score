package com.alancamargo.snookerscore.ui.viewmodel.playerlist

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.model.Gender
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.usecase.player.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.UUID

class PlayerListViewModel(
    private val isPickingPlayer: Boolean,
    private val addOrUpdatePlayerUseCase: AddOrUpdatePlayerUseCase,
    private val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerListUiState, PlayerListUiAction>(initialState = PlayerListUiState()) {

    private var playerId = ""
    private var playerName = ""
    private var playerGenderOrdinal = -1

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
            }
        }
    }

    private suspend fun <T> Flow<T>.handleFlow(block: (T) -> Unit) {
        flowOn(dispatcher)
            .onStart {
                sendAction { PlayerListUiAction.ShowLoading }
            }.onCompletion {
                sendAction { PlayerListUiAction.HideLoading }
            }.catch {
                sendAction { PlayerListUiAction.ShowError }
            }.collect {
                block(it)
            }
    }

}
