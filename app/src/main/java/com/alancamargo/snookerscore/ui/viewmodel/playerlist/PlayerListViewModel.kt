package com.alancamargo.snookerscore.ui.viewmodel.playerlist

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.usecase.player.AddOrUpdatePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DeletePlayerUseCase
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
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerListUiState, PlayerListUiAction>(initialState = PlayerListUiState()) {

    fun onNewPlayerClicked() {
        sendAction { PlayerListUiAction.ShowNewPlayerDialogue }
    }

    fun onPlayerSelected(player: UiPlayer) {
        sendAction { PlayerListUiAction.SelectPlayer(player) }
    }

    fun onSavePlayerClicked(player: UiPlayer) {
        viewModelScope.launch {
            addOrUpdatePlayerUseCase(player.toDomain()).getPlayersOnSuccess()
        }
    }

    fun onDeletePlayerClicked(player: UiPlayer) {
        viewModelScope.launch {
            deletePlayerUseCase(player.toDomain()).getPlayersOnSuccess()
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
