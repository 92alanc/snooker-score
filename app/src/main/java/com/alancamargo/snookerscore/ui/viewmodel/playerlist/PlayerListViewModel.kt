package com.alancamargo.snookerscore.ui.viewmodel.playerlist

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.usecase.player.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
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

class PlayerListViewModel(
    private val addOrUpdatePlayerUseCase: AddOrUpdatePlayerUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerListUiState, PlayerListUiAction>(initialState = PlayerListUiState()) {

    fun onNewPlayerClicked() {
        sendAction { PlayerListUiAction.ShowNewPlayerDialogue }
    }

    fun onPlayerClicked(player: UiPlayer) {
        sendAction { PlayerListUiAction.OpenPlayerStats(player) }
    }

    fun onSavePlayerClicked(player: UiPlayer) {
        viewModelScope.launch {
            addOrUpdatePlayerUseCase(player.toDomain()).getPlayersOnSuccess()
        }
    }

    fun getPlayers() {
        viewModelScope.launch {
            getPlayersUseCase().flowOn(dispatcher)
                .onStart {
                    sendAction { PlayerListUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { PlayerListUiAction.HideLoading }
                }.catch {
                    sendAction { PlayerListUiAction.ShowError }
                }.collect {
                    val players = it.map { domainPlayer -> domainPlayer.toUi() }
                    setState { state -> state.onPlayersReceived(players) }
                }
        }
    }

    private suspend fun Flow<Unit>.getPlayersOnSuccess() {
        flowOn(dispatcher)
            .onStart {
                sendAction { PlayerListUiAction.ShowLoading }
            }.onCompletion {
                sendAction { PlayerListUiAction.HideLoading }
            }.catch {
                sendAction { PlayerListUiAction.ShowError }
            }.collect {
                getPlayers()
            }
    }

}
