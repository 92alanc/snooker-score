package com.alancamargo.snookerscore.features.match.ui.viewmodel.newmatch

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.frame.domain.model.Frame
import com.alancamargo.snookerscore.features.match.domain.model.Match
import com.alancamargo.snookerscore.features.match.domain.usecase.AddMatchUseCase
import com.alancamargo.snookerscore.features.match.ui.model.PlayerToPick
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.alancamargo.snookerscore.features.player.domain.usecase.ArePlayersTheSameUseCase
import com.alancamargo.snookerscore.features.player.ui.mapping.toDomain
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer
import com.alancamargo.snookerscore.features.frame.ui.mapping.toUi
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val MIN_NUMBER_OF_FRAMES = 1
private const val MAX_NUMBER_OF_FRAMES = 35

class NewMatchViewModel(
    private val arePlayersTheSameUseCase: ArePlayersTheSameUseCase,
    private val addMatchUseCase: AddMatchUseCase,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<NewMatchUiState, NewMatchUiAction>(
    initialState = NewMatchUiState(numberOfFrames = MIN_NUMBER_OF_FRAMES)
) {

    private val frames = mutableListOf<UiFrame>()

    private var player1: UiPlayer? = null
    private var player2: UiPlayer? = null
    private var numberOfFrames = 1
    private var playerToPick = PlayerToPick.PLAYER_1

    fun onSelectPlayer1ButtonClicked() {
        playerToPick = PlayerToPick.PLAYER_1
        sendAction { NewMatchUiAction.PickPlayer }
    }

    fun onSelectPlayer2ButtonClicked() {
        playerToPick = PlayerToPick.PLAYER_2
        sendAction { NewMatchUiAction.PickPlayer }
    }

    fun onPlayerSelected(player: UiPlayer) {
        if (playerToPick == PlayerToPick.PLAYER_1) {
            onPlayer1Selected(player)
        } else {
            onPlayer2Selected(player)
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

    fun onNumberOfFramesIncreased() {
        if (numberOfFrames < MAX_NUMBER_OF_FRAMES) {
            numberOfFrames += 2
            setState { state -> state.onNumberOfFramesChanged(numberOfFrames) }
        }
    }

    fun onNumberOfFramesDecreased() {
        if (numberOfFrames > MIN_NUMBER_OF_FRAMES) {
            numberOfFrames -= 2
            setState { state -> state.onNumberOfFramesChanged(numberOfFrames) }
        }
    }

    private fun onPlayer1Selected(player1: UiPlayer) {
        this.player1 = player1
        setState { state -> state.onPlayer1Selected(player1) }

        if (player2 != null) {
            setState { state -> state.onEnableStartMatchButton() }
        } else {
            setState { state -> state.onDisableStartMatchButton() }
        }
    }

    private fun onPlayer2Selected(player2: UiPlayer) {
        this.player2 = player2
        setState { state -> state.onPlayer2Selected(player2) }

        if (player1 != null) {
            setState { state -> state.onEnableStartMatchButton() }
        } else {
            setState { state -> state.onDisableStartMatchButton() }
        }
    }

    private fun createMatch(player1: Player, player2: Player) {
        viewModelScope.launch {
            val match = Match(player1 = player1, player2 = player2, numberOfFrames = numberOfFrames)
            addMatchUseCase(match).handleFlow {
                logger.debug("Match created: $match")
                createFrames(match)
                sendAction { NewMatchUiAction.StartMatch(frames) }
            }
        }
    }

    private suspend fun <T> Flow<T>.handleFlow(onCollect: () -> Unit) {
        return flowOn(dispatcher).catch { throwable ->
            logger.error(throwable)
            sendAction { NewMatchUiAction.ShowError }
        }.collect {
            onCollect()
        }
    }

    private fun createFrames(match: Match) {
        repeat(match.numberOfFrames) { index ->
            val frame = Frame(
                positionInMatch = index + 1,
                match = match
            )

            frames.add(frame.toUi())
        }
    }

}
