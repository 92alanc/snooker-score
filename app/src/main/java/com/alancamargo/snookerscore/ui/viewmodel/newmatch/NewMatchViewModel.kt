package com.alancamargo.snookerscore.ui.viewmodel.newmatch

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.model.Match
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.usecase.match.AddMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetPlayersUseCase
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

class NewMatchViewModel(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val arePlayersTheSameUseCase: ArePlayersTheSameUseCase,
    private val addMatchUseCase: AddMatchUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<NewMatchUiState, NewMatchUiAction>(initialState = NewMatchUiState()) {

    private var player1: UiPlayer? = null
    private var player2: UiPlayer? = null
    private var numberOfFrames = 0

    fun onFieldErased() {
        setState { state -> state.onDisableStartMatchButton() }
    }

    fun onAllFieldsFilled() {
        setState { state -> state.onEnableStartMatchButton() }
    }

    fun onStartMatchButtonClicked(player1: UiPlayer, player2: UiPlayer, numberOfFrames: Int) {
        this.player1 = player1
        this.player2 = player2
        this.numberOfFrames = numberOfFrames

        val domainPlayer1 = player1.toDomain()
        val domainPlayer2 = player2.toDomain()

        val arePlayersTheSame = arePlayersTheSameUseCase(domainPlayer1, domainPlayer2)

        if (arePlayersTheSame) {
            sendAction { NewMatchUiAction.ShowSamePlayersDialogue }
        } else {
            createMatch(domainPlayer1, domainPlayer2)
        }
    }

    fun onHelpButtonClicked() {
        sendAction { NewMatchUiAction.ShowHelp }
    }

    fun getPlayers() {
        viewModelScope.launch {
            getPlayersUseCase().flowOn(dispatcher)
                .onStart {
                    sendAction { NewMatchUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { NewMatchUiAction.HideLoading }
                }.catch {
                    sendAction { NewMatchUiAction.ShowError }
                }.collect { players ->
                    val uiPlayers = players.map { it.toUi() }
                    setState { state -> state.onPlayersReceived(uiPlayers) }
                }
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
