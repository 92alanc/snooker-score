package com.alancamargo.snookerscore.features.playerstats.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.player.domain.usecase.DeletePlayerUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toDomain
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.features.playerstats.domain.usecase.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.features.playerstats.ui.mapping.toUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PlayerStatsViewModel(
    private val getPlayerStatsUseCase: GetPlayerStatsUseCase,
    private val deletePlayerUseCase: DeletePlayerUseCase,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerStatsUiState, PlayerStatsUiAction>(initialState = PlayerStatsUiState()) {

    fun getPlayerStats(player: UiPlayer) {
        viewModelScope.launch {
            getPlayerStatsUseCase(player.toDomain()).flowOn(dispatcher)
                .catch { throwable ->
                    logger.error(throwable)
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
                .catch { throwable ->
                    logger.error(throwable)
                    sendAction { PlayerStatsUiAction.ShowError }
                }.collect {
                    sendAction { PlayerStatsUiAction.Finish }
                }
        }
    }

}
