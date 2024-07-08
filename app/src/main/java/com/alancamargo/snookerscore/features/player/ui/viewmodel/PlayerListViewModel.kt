package com.alancamargo.snookerscore.features.player.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.core.data.preferences.PreferenceManager
import com.alancamargo.snookerscore.features.player.data.analytics.PlayerListAnalytics
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
    private val useCases: UseCases,
    private val analytics: PlayerListAnalytics,
    private val preferenceManager: PreferenceManager,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<PlayerListUiState, PlayerListUiAction>(initialState = PlayerListUiState()) {

    private var playerId = ""
    private var playerName = ""
    private var playerGenderOrdinal = -1

    init {
        analytics.trackScreenViewed()
    }

    fun onDontShowTipAgainClicked() {
        analytics.trackDoNotShowTipAgainClicked()
        preferenceManager.dontShowPlayerListTipAgain()
    }

    fun onTipDismissed() {
        analytics.trackTipDismissed()
    }

    fun onNewPlayerClicked() {
        analytics.trackNewPlayerClicked()

        playerId = UUID.randomUUID().toString()
        sendAction { PlayerListUiAction.ShowNewPlayerDialogue }
    }

    fun onPlayerClicked(player: UiPlayer) {
        analytics.trackPlayerCardClicked()

        val action = if (isPickingPlayer) {
            PlayerListUiAction.PickPlayer(player)
        } else {
            PlayerListUiAction.OpenPlayerStats(player)
        }

        sendAction { action }
    }

    fun onPlayerLongClicked(player: UiPlayer) {
        analytics.trackPlayerCardLongClicked()

        playerId = player.id
        sendAction { PlayerListUiAction.EditPlayer(player) }
    }

    fun onSavePlayerClicked() {
        analytics.trackSavePlayerClicked()

        if (playerName.isBlank() || playerGenderOrdinal < 0) {
            sendAction { PlayerListUiAction.ShowError }
            return
        }

        val gender = Gender.entries[playerGenderOrdinal]
        val player = Player(id = playerId, name = playerName, gender = gender)

        viewModelScope.launch {
            useCases.addOrUpdatePlayerUseCase(player).handleFlow {
                viewModelScope.launch {
                    useCases.addOrUpdatePlayerStatsUseCase(player).handleFlow {
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
            useCases.getPlayersUseCase().handleFlow {
                val players = it.map { domainPlayer -> domainPlayer.toUi() }
                setState { state -> state.onPlayersReceived(players) }

                if (players.isNotEmpty() && preferenceManager.shouldShowPlayerListTip()) {
                    sendAction { PlayerListUiAction.ShowTip }
                }
            }
        }
    }

    fun onBackClicked() {
        analytics.trackBackClicked()
        sendAction { PlayerListUiAction.Finish }
    }

    fun onNativeBackClicked() {
        analytics.trackNativeBackClicked()
        sendAction { PlayerListUiAction.Finish }
    }

    private suspend fun <T> Flow<T>.handleFlow(block: (T) -> Unit) {
        flowOn(dispatcher).catch { throwable ->
            logger.error(throwable)
            sendAction { PlayerListUiAction.ShowError }
        }.collect {
            block(it)
        }
    }

    class UseCases(
        val addOrUpdatePlayerUseCase: AddOrUpdatePlayerUseCase,
        val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase,
        val getPlayersUseCase: GetPlayersUseCase,
    )

}
