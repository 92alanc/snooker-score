package com.alancamargo.snookerscore.ui.viewmodel.playerstats

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.usecase.player.DeletePlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PlayerStatsViewModel(
    private val getPlayerStatsUseCase: GetPlayerStatsUseCase,
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerStatsUiState, PlayerStatsUiAction>(initialState = PlayerStatsUiState()) {

    fun getPlayerStats(player: UiPlayer) {
        viewModelScope.launch {
            getPlayerStatsUseCase(player.toDomain()).flowOn(dispatcher)
                .onStart {
                    sendAction { PlayerStatsUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { PlayerStatsUiAction.HideLoading }
                }.catch {
                    sendAction { PlayerStatsUiAction.ShowError }
                }.collect {
                    val playerStats = it.toUi()
                    setState { state -> state.onPlayerStatsReceived(playerStats) }
                }
        }
    }

    fun onDeletePlayerClicked() {
        sendAction { PlayerStatsUiAction.ShowDeletePlayerConfirmation }
    }

    fun onDeletePlayerConfirmed(player: UiPlayer) {
        viewModelScope.launch {
            deletePlayerUseCase(player.toDomain()).flowOn(dispatcher)
                .onStart {
                    sendAction { PlayerStatsUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { PlayerStatsUiAction.HideLoading }
                }.catch {
                    sendAction { PlayerStatsUiAction.ShowError }
                }.collect {
                    sendAction { PlayerStatsUiAction.Finish }
                }
        }
    }

}
