package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.usecase.match.AddMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.ArePlayersTheSameUseCase
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

private const val INITIAL_NUMBER_OF_FRAMES = 1

class NewMatchViewModel(
    private val arePlayersTheSameUseCase: ArePlayersTheSameUseCase,
    private val addMatchUseCase: AddMatchUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<NewMatchUiState, NewMatchUiAction>(
    initialState = NewMatchUiState(numberOfFrames = INITIAL_NUMBER_OF_FRAMES)
) {

    private var player1: UiPlayer? = null
    private var player2: UiPlayer? = null
    private var numberOfFrames = 1

    fun onSelectPlayerButtonClicked() {
        sendAction { NewMatchUiAction.ShowPlayers }
    }

    fun onPlayer1Selected(player1: UiPlayer) {
        this.player1 = player1
        setState { state -> state.onPlayer1Selected(player1) }

        if (player2 != null) {
            setState { state -> state.onEnableStartMatchButton() }
        } else {
            setState { state -> state.onDisableStartMatchButton() }
        }
    }

    fun onPlayer2Selected(player2: UiPlayer) {
        this.player2 = player2
        setState { state -> state.onPlayer2Selected(player2) }

        if (player1 != null) {
            setState { state -> state.onEnableStartMatchButton() }
        } else {
            setState { state -> state.onDisableStartMatchButton() }
        }
    }

    fun onStartMatchButtonClicked() {
        player1?.let { p1 ->
            player2?.let { p2 ->
                val domainPlayer1 = p1.toDomain()
                val domainPlayer2 = p2.toDomain()

                val arePlayersTheSame = arePlayersTheSameUseCase(domainPlayer1, domainPlayer2)

                if (arePlayersTheSame) {
                    sendAction { NewMatchUiAction.ShowSamePlayersDialogue }
                } else {
                    createMatch(domainPlayer1, domainPlayer2)
                }
            }
        } ?: run { sendAction { NewMatchUiAction.ShowError } }
    }

    fun onHelpButtonClicked() {
        sendAction { NewMatchUiAction.ShowHelp }
    }

    fun onNumberOfFramesIncreased() {
        numberOfFrames += 2
        setState { state -> state.onNumberOfFramesChanged(numberOfFrames) }
    }

    fun onNumberOfFramesDecreased() {
        if (numberOfFrames > INITIAL_NUMBER_OF_FRAMES) {
            numberOfFrames -= 2
            setState { state -> state.onNumberOfFramesChanged(numberOfFrames) }
        }
    }

    private fun createMatch(player1: Player, player2: Player) {
        viewModelScope.launch {
            val match = Match(player1 = player1, player2 = player2, numberOfFrames = numberOfFrames)
            addMatchUseCase(match).flowOn(dispatcher)
                .onStart {
                    sendAction { NewMatchUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { NewMatchUiAction.HideLoading }
                }.catch {
                    sendAction { NewMatchUiAction.ShowError }
                }.collect {
                    val uiMatch = match.toUi()
                    sendAction { NewMatchUiAction.StartMatch(uiMatch) }
                }
        }
    }

}
